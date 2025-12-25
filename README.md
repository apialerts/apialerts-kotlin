# API Alerts • Kotlin Multiplatform Client

[![Platform](https://img.shields.io/badge/kotlin-android%20%7C%20jvm%20%7C%20js%20%7C%20ios/macos%20(arm64)-blue)](https://github.com/apialerts/apialerts-kotlin)

[GitHub Repo](https://github.com/apialerts/apialerts-kotlin) • [Maven](https://central.sonatype.com/artifact/com.apialerts/client/overview)

This is a Kotlin Multiplatform library that allows you to send alerts from any supported platform, including:
- Android
- JVM 11+ (Java/Kotlin backends)
- iOS / macOS (Apple Silicon / arm64 only)
- JavaScript (Browser & Node.js)

## Installation

The recommended way to add the dependency is by using the Gradle Version Catalog (`libs.versions.toml`).

**1. Add the dependency to your `libs.versions.toml` file:**
```toml
[versions]
apialerts = "1.1.0-alpha02"

[libraries]
apialerts-client = { module = "com.apialerts:client", version.ref = "apialerts" }
```

**2. Apply the dependency in your `build.gradle.kts`:**
```kotlin
// In your commonMain source set
sourceSets {
    commonMain.dependencies {
        implementation(libs.apialerts.client)
    }
}
```

Ensure `mavenCentral()` is added to your repository list in `settings.gradle.kts`.

> **A note for Swift Developers:** If you are working in a native Swift project, we recommend using our dedicated [apialerts-swift](https://github.com/apialerts/apialerts-swift) library for the most idiomatic experience. This kotlin library is intended for use in Kotlin Multiplatform projects.

### Alternative: Manual Dependency Declaration

If you are not using the version catalog, you can add the dependency directly:
```kotlin
// In build.gradle.kts
implementation("com.apialerts:client:1.1.0-alpha02")

// In build.gradle (Groovy)
implementation 'com.apialerts:client:1.1.0-alpha02'
```

### Client Initialization

You must initialize the client with your API key before sending alerts. This is best done once when your application starts.

```kotlin
ApiAlerts.configure(
    apiKey = "your-api-key",
    debug = true // Optional: Enables console logging for requests
)
```
This sets the default API key that will be used for all subsequent requests.

### Send Events

You can send alerts using a simple function call or a DSL-style builder.

**Simple Style**
```kotlin
ApiAlerts.send(
  channel = "your-channel",       // Optional, uses the default workspace channel if not provided
  message = "Your alert message",
  tags = listOf("tag1", "tag2"),  // Optional tags
  link = "https://example.com"    // Optional link
)
```

**DSL Style**
```kotlin
ApiAlerts.send {
  channel = "your-channel"        // Optional, uses the default workspace channel if not provided
  message = "Your alert message"
  tags = listOf("tag1", "tag2")   // Optional tags
  link = "https://example.com"    // Optional link
}
```

If you need to use a different API key for a specific alert, you can pass it as a parameter to override the default:
```kotlin
ApiAlerts.send(apiKey = "a-different-api-key", message = "Your alert message")
```

### Fire-and-Forget vs Suspending

The library provides two ways to send alerts:
- `send()`: __Recommended for 99% of users__. This is a "fire-and-forget" function that returns immediately and sends the alert on a background thread. This is the recommended method for most UI-based applications (Android, iOS, Mac, Web).
- `sendAsync()`: This is a `suspend` function that will wait for the network request to complete. It is best suited for serverless backend environments where you need to ensure the alert has been sent before the process terminates.
