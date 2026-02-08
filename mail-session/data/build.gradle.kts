import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/*
 * Copyright (c) 2022 Proton Technologies AG
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

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("app-config-plugin")
}

android {
    namespace = "ch.protonmail.android.mailsession.data"
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
    kapt(libs.bundles.app.annotationProcessors)
    compileOnly(libs.proton.rust.core)
    implementation(libs.bundles.module.data)
    implementation(libs.androidx.hilt.work)
    implementation(libs.proton.core.crypto)
    implementation(libs.proton.core.domain)
    implementation(libs.proton.core.network.domain)
    implementation(libs.proton.core.utilAndroidDevice)

    implementation(project(":mail-featureflags:domain"))
    implementation(project(":mail-bugreport:domain"))
    implementation(project(":mail-common:data"))
    implementation(project(":mail-common:domain"))
    implementation(project(":mail-session:domain"))
    implementation(project(":shared:core:account:domain"))
    implementation(project(":shared:core:humanverification:domain"))

    testImplementation(project(":test:utils"))
    testImplementation(project(":test:test-data"))
    testImplementation(libs.proton.rust.core)
    testImplementation(libs.bundles.test)
}
