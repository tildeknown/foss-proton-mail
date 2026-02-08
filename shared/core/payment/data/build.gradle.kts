import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/*
 * Copyright (c) 2025 Proton AG
 * This file is part of Proton AG and Proton Mail.
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

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("app-config-plugin")
}

android {
    namespace = "me.proton.android.core.payment.rust.data"

    compileSdk = AppConfiguration.compileSdk.get()

    defaultConfig {
        minSdk = AppConfiguration.minSdk.get()
        lint.targetSdk = AppConfiguration.targetSdk.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget("17")
        }
    }
}

dependencies {
    compileOnly(libs.proton.rust.core)

    implementation(libs.dagger.hilt.android)

    implementation(libs.proton.core.user.domain)
    implementation(libs.proton.core.presentation) // formatCentsPriceDefaultLocale

    implementation(project(":mail-session:domain"))
    implementation(project(":shared:core:payment:domain"))
    implementation(project(":shared:core:payment:presentation"))

    kapt(libs.bundles.app.annotationProcessors)
}
