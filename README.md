# android-versions
[Mobile] VersionsX

# Android Versions Catalog

This repository publishes a `mobilex.versions.toml` file specifically designed for Android projects. The version catalog helps manage dependencies across multiple Android projects by centralizing version definitions, making it easier to maintain consistency.

## Overview

The `mobilex.versions.toml` file in this repository contains version declarations for libraries and dependencies used in Android projects. By consuming this file, you can reduce redundancy and simplify version management across your projects.

## Setup Instructions

To consume the version catalog in your Android project, follow these steps:

### 1. Add the Version Catalog to Your Project

In your project’s `settings.gradle.kts`, add a dependency on the `android-versions` repository hosted on GitHub Packages:

```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from("vn.core.libx:android-versions:1.0.0")
        }
    }
}
```
### 2. Configure GitHub Packages Authentication

To access the `android-versions` catalog from GitHub Packages, you need to configure authentication using a personal access token (PAT).

- Generate a personal access token by following [GitHub's instructions](https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token).
- In your `gradle.properties` or `local.properties` file, add the following entries:

```properties
USERNAME=GITHUB_USERNAME
TOKEN=PERSONAL_ACCESS_TOKEN
