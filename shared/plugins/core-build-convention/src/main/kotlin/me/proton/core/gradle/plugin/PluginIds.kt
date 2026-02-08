/*
 * Copyright (c) 2021 Proton Technologies AG
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

package me.proton.core.gradle.plugin

internal object PluginIds {
    const val ANDROID_APP = "com.android.application"
    const val ANDROID_LIBRARY = "com.android.library"
    const val ANDROID_TEST = "com.android.test"
    const val HILT = "dagger.hilt.android.plugin"
    const val JAVA_LIBRARY = "org.gradle.java-library"
    const val KAPT = "org.jetbrains.kotlin.kapt"
    const val KOTLIN_ANDROID = "org.jetbrains.kotlin.android"
    const val KOTLIN_JVM = "org.jetbrains.kotlin.jvm"
    const val PAPARAZZI = "app.cash.paparazzi"
}
