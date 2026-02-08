import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import studio.forface.easygradle.dsl.compileOnly

/*
 * Copyright (C) 2024 Proton AG
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.plugin.compose")
    id("app.cash.paparazzi")
    id("app-config-plugin")
}

android {
    namespace = "me.proton.android.core.devicemigration.presentation"

    compileSdk = AppConfiguration.compileSdk.get()

    defaultConfig {
        minSdk = AppConfiguration.minSdk.get()
        lint.targetSdk = AppConfiguration.targetSdk.get()
        testInstrumentationRunner = AppConfiguration.testInstrumentationRunner.get()
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

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

kotlin {
    explicitApi()
}

dependencies {
    compileOnly(libs.proton.rust.core)

    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.hilt.lifecycle.viewmodel)
    implementation(libs.dagger.hilt.android)
    implementation(libs.proton.core.biometric)
    implementation(libs.proton.core.domain)
    implementation(libs.proton.core.presentation)
    implementation(libs.proton.core.presentationCompose)
    implementation(libs.proton.core.utilKotlin)
    implementation(libs.zxing.embedded)

    implementation(project(":design-system"))
    implementation(project(":mail-session:domain"))
    implementation(project(":shared:core:account:domain"))

    kapt(libs.bundles.app.annotationProcessors)
}
