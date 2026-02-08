/*
 * Copyright (c) 2024 Proton Technologies AG
 * This file is part of Proton Technologies AG and ProtonCore.
 *
 * ProtonCore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonCore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.proton.core.paymentiap.data.worker

import android.app.Activity
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import me.proton.core.observability.domain.ObservabilityContext
import me.proton.core.observability.domain.ObservabilityManager
import me.proton.core.observability.domain.metrics.CheckoutGiapBillingAcknowledgeTotal
import me.proton.core.payment.domain.entity.Purchase
import me.proton.core.payment.domain.entity.PurchaseState
import me.proton.core.payment.domain.isRecoverable
import me.proton.core.payment.domain.repository.GoogleBillingRepository
import me.proton.core.payment.domain.repository.PurchaseRepository
import me.proton.core.payment.domain.usecase.FindGooglePurchaseForPaymentOrderId
import me.proton.core.paymentiap.domain.toGiapStatus
import me.proton.core.plan.domain.LogTag
import me.proton.core.util.android.workmanager.builder.setExpeditedIfPossible
import me.proton.core.util.kotlin.CoreLogger
import me.proton.core.util.kotlin.coroutine.withResultContext
import javax.inject.Provider

//region Constants

private const val TAG: String = "AcknowledgePurchaseWorker"
private const val INPUT_PLAN_NAME: String = "arg.planName"

//endregion

@HiltWorker
internal class AcknowledgePurchaseWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val findGooglePurchase: FindGooglePurchaseForPaymentOrderId,
    private val purchaseRepository: PurchaseRepository,
    private val googleBillingRepository: Provider<GoogleBillingRepository<Activity>>,
    override val observabilityManager: ObservabilityManager
) : CoroutineWorker(context, params), ObservabilityContext {

    override suspend fun doWork(): Result {
        return withResultContext {
            onResultEnqueueObservability("acknowledgePurchase") {
                CheckoutGiapBillingAcknowledgeTotal(toGiapStatus())
            }

            val planName = requireNotNull(inputData.getString(INPUT_PLAN_NAME))
            val purchase = requireNotNull(purchaseRepository.getPurchase(planName))

            runCatching {
                val googlePurchase = requireNotNull(findGooglePurchase(purchase.paymentOrderId))

                googleBillingRepository.get().use { billingRepository ->
                    /**
                     * The server will acknowledge, therefore it's possible the purchase has
                     * already been acknowledged. The documentation stipulates that one should check
                     * if a purchase is already acknowledged.
                     */
                    if (!googlePurchase.isAcknowledged) {
                        billingRepository.acknowledgePurchase(googlePurchase.purchaseToken)
                    }
                }
            }.fold(
                onSuccess = { onSuccess(purchase) },
                onFailure = { error -> onFailure(purchase, error) }
            )
        }
    }

    private suspend fun onSuccess(purchase: Purchase): Result {
        CoreLogger.w(LogTag.PURCHASE_INFO, "$TAG, acknowledged: $purchase")

        purchaseRepository.upsertPurchase(purchase.copy(purchaseState = PurchaseState.Acknowledged))
        return Result.success()
    }

    private suspend fun onFailure(purchase: Purchase, error: Throwable): Result {
        return if (error.isRecoverable()) {
            CoreLogger.w(LogTag.PURCHASE_INFO, "$TAG, retrying: $purchase")

            Result.retry()
        } else {
            CoreLogger.e(LogTag.PURCHASE_ERROR, "$TAG, permanent failure: $purchase")

            setPurchaseFailed(purchase, error)
            Result.failure()
        }
    }

    private suspend fun setPurchaseFailed(purchase: Purchase, error: Throwable) {
        purchaseRepository.upsertPurchase(
            purchase.copy(
                purchaseFailure = error.message,
                purchaseState = PurchaseState.Failed
            )
        )
    }

    companion object {

        fun getOneTimeUniqueWorkName(planName: String): String {
            return "$TAG-$planName"
        }

        fun getRequest(planName: String): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<AcknowledgePurchaseWorker>()
                .setInputData(workDataOf(INPUT_PLAN_NAME to planName))
                .setExpeditedIfPossible()
                .build()
        }
    }
}
