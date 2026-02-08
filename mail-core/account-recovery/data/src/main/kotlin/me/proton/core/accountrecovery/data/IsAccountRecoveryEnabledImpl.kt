package me.proton.core.accountrecovery.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import me.proton.core.accountrecovery.domain.IsAccountRecoveryEnabled
import me.proton.core.featureflag.data.IsFeatureFlagEnabledImpl
import me.proton.core.featureflag.domain.FeatureFlagManager
import me.proton.core.featureflag.domain.entity.FeatureId
import javax.inject.Inject

public class IsAccountRecoveryEnabledImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    featureFlagManager: FeatureFlagManager
) : IsAccountRecoveryEnabled, IsFeatureFlagEnabledImpl(
    context = context,
    featureFlagManager = featureFlagManager,
    featureId = featureId,
    localFlagId = R.bool.core_feature_account_recovery_enabled
) {
    internal companion object {
        val featureId = FeatureId("SignedInAccountRecovery")
    }
}
