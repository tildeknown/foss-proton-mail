Proton Mail for Android
=======================
Copyright (c) 2026 Proton Technologies AG

## Contributing
We’ve recently rebuilt the app and are focused on our current roadmap of features and fixes. Therefore, we are not accepting new issues or PRs at the moment: feel free to fork the repo for your own experiments. We appreciate your understanding and support.

## Build instructions
- Install and configure the environment (two options available)
  - [Android Studio bundle](https://developer.android.com/studio/install)
  - [Standalone Android tools](https://developer.android.com/tools)
- Install and configure Java 17+ (not needed for Android Studio bundle as it's included)
  - Install Java 17 with `brew install openjdk@17` | `apt install openjdk-17-jdk`
  - Set Java 17 as the current version by using the `JAVA_HOME` environment variable
- Clone this repository (Use `git clone git@github.com:ProtonMail/android-mail.git`.)
- Ensure [Git LFS](https://git-lfs.com/) is installed (`git lfs install`) for snapshot test assets
- Setup `google-services.json` file by running `./scripts/setup_google_services.sh`
- Build with any of the following:
  - Execute `./gradlew assembleAlphaDebug` in a terminal
  - Open Android Studio and build the `:app` module

## CI / CD
CI stages are defined in the `.gitlab-ci.yml` file and we rely on [fastlane](https://docs.fastlane.tools/) to implement most of them.
Fastlane can be installed and used locally by performing
```
bundle install
```
(requires Ruby and `bundler` to be available locally)
```
bundle exec fastlane lanes
```
will show all the possible actions that are available.

## Deploy
Each merge to `main` branch builds the branch's HEAD and deploys it
to [Firebase App Distribution](https://firebase.google.com/docs/app-distribution).

## Observability

We use **Sentry** for crash/error reporting (primarily for **non-debuggable** builds).

- **Runtime crash/error reporting**: the DSN is injected at build time via environment variables (`SENTRY_DSN_MAIL` and
  `SENTRY_DSN_ACCOUNT`). If these variables are not set (typical for local builds and forks), Sentry is effectively
  disabled.
- **Mapping/symbol uploads (CI / release tasks)**: CI provides a `sentry.properties` file for uploading ProGuard/R8
  mappings and native symbols.

License
-------
The code and data files in this distribution are licensed under the terms of the GPLv3 as published by the Free Software
Foundation. See [the GPLv3 license text](https://www.gnu.org/licenses/) for a copy of this license.

