# Proton Mail for Android

FOSS version

## TODO-list

- [x] remove detekt
- [x] remove fastlane
- [ ] remove firebase
- [x] remove sentry
- [ ] remove google services
- [ ] remove unit-tests
- [ ] copy all the proton remote dependencies
- [ ] build workflow for github actions

## Build instructions
- Install and configure the environment (two options available)
  - [Android Studio bundle](https://developer.android.com/studio/install)
  - [Standalone Android tools](https://developer.android.com/tools)
- Install and configure Java 17+ (not needed for Android Studio bundle as it's included)
  - Install Java 17 with `brew install openjdk@17` | `apt install openjdk-17-jdk`
  - Set Java 17 as the current version by using the `JAVA_HOME` environment variable
- Clone this repository (Use `git clone git@github.com:tildeknown/foss-proton-mail.git`)
- Ensure [Git LFS](https://git-lfs.com/) is installed (`git lfs install`) for snapshot test assets
- Setup `google-services.json` file by running `./scripts/setup_google_services.sh`
- Build with any of the following:
  - Execute `./gradlew assembleAlphaDebug` to build the debug version
  - Execute `./gradlew build` to build the release version

License
-------
The code and data files in this distribution are licensed under the terms of the GPLv3 as published by the Free Software
Foundation. See [the GPLv3 license text](https://www.gnu.org/licenses/) for a copy of this license.

Copyright (c) 2026 Proton Technologies AG \
Copyright (c) 2026 Tildeguy (tildeguy@mainlining.org)
