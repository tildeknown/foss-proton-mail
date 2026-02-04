/*
 * Copyright (c) 2025 Proton Technologies AG
 * This file is part of Proton Technologies AG and Proton Mail.
 *
 * Proton Mail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Mail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Mail. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.protonmail.android.mailupselling.dagger

import android.content.Context
import ch.protonmail.android.mailupselling.data.BlackFridayDataStoreProvider
import ch.protonmail.android.mailupselling.data.local.BlackFridayLocalDataSource
import ch.protonmail.android.mailupselling.data.local.BlackFridayLocalDataSourceImpl
import ch.protonmail.android.mailupselling.data.repository.BlackFridayRepositoryImpl
import ch.protonmail.android.mailupselling.domain.annotation.UpsellingCacheScope
import ch.protonmail.android.mailupselling.domain.repository.BlackFridayRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UpsellingModule {

    @Binds
    @Reusable
    fun provideBlackFridayRepository(impl: BlackFridayRepositoryImpl): BlackFridayRepository

    @Binds
    @Singleton
    fun provideBlackFridayDataSource(impl: BlackFridayLocalDataSourceImpl): BlackFridayLocalDataSource

    @Module
    @InstallIn(SingletonComponent::class)
    object UpsellingModuleProvider {

        @Provides
        @Singleton
        fun provideDataStoreProvider(@ApplicationContext context: Context): BlackFridayDataStoreProvider =
            BlackFridayDataStoreProvider(context)
    }
}
