import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.android.tools.build)
        classpath(libs.kotlin.gradle)
        classpath(libs.hilt.android.gradle)
        classpath(libs.paparazzi.plugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    id("studio.forface.easygradle") version "3.0.5" apply false
}

subprojects {
    plugins.apply("studio.forface.easygradle")

    if (project.findProperty("enableComposeCompilerReports") == "true") {
        plugins.withId("org.jetbrains.kotlin.android") {
            tasks.withType<KotlinCompile>().configureEach {
                val reportsPrefix =
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination="
                val metricsPrefix =
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination="

                val projectDir = project.layout.buildDirectory.asFile.get().absolutePath
                val currentArgs = compilerOptions.freeCompilerArgs.get()

                // Avoid adding them twice if this block runs multiple times
                if (currentArgs.none { it.startsWith(reportsPrefix) } &&
                    currentArgs.none { it.startsWith(metricsPrefix) }
                ) {
                    compilerOptions.freeCompilerArgs.set(
                        currentArgs + listOf(
                            "-P",
                            "$reportsPrefix$projectDir/compose_reports",
                            "-P",
                            "$metricsPrefix$projectDir/compose_metrics"
                        )
                    )
                }
            }
        }
    }


    // plugins.withId("com.android.application") {
    //     extensions.configure<com.android.build.gradle.AppExtension> {
    //         lint {
    //             checkReleaseBuilds = false
    //             abortOnError = false
    //         }
    //     }
    // }

    plugins.withId("com.android.library") {
        extensions.configure<com.android.build.gradle.LibraryExtension> {
            lint {
                checkReleaseBuilds = false
                abortOnError = false
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

kotlinCompilerArgs(
    "-opt-in=kotlin.RequiresOptIn",
    // Enables experimental Coroutines API.
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    // Enables experimental Time (Turbine).
    "-opt-in=kotlin.time.ExperimentalTime",
    // https://youtrack.jetbrains.com/issue/KT-73255
    "-Xannotation-default-target=param-property",
    // Global Compose stability configuration
    "-P",
    "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=" +
        rootProject.layout.projectDirectory
            .file("compose-stability.conf")
            .asFile
            .absolutePath
)

fun Project.kotlinCompilerArgs(vararg extraCompilerArgs: String) {
    for (sub in subprojects) {
        sub.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            compilerOptions {
                freeCompilerArgs.addAll(extraCompilerArgs.toList())
            }
        }
    }
}
