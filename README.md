# API Alerts • Kotlin Multiplatform Client

[![Maven Central](https://img.shields.io/maven-central/v/com.apialerts/client)](https://central.sonatype.com/artifact/com.apialerts/client)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[Maven Central](https://central.sonatype.com/artifact/com.apialerts/client) • [GitHub](https://github.com/apialerts/apialerts-kotlin) • [API Alerts](https://apialerts.com)

Effortless project notifications. Send once, deliver everywhere.

Kotlin Multiplatform library — works across Android, JVM (Java/Kotlin), iOS, macOS, JavaScript, and WebAssembly from a single dependency.

## Supported Platforms

| Platform | Target |
|----------|--------|
| Android  | `androidTarget` |
| JVM      | `jvm` (Java 11+) |
| iOS      | `iosArm64`, `iosX64`, `iosSimulatorArm64` |
| macOS    | `macosArm64`, `macosX64` |
| JavaScript | `js` (browser + Node.js) |
| WebAssembly | `wasmJs` (browser + Node.js) |

## Installation

Add the dependency to your `libs.versions.toml`:

```toml
[versions]
apialerts = "2.0.0"

[libraries]
apialerts-client = { module = "com.apialerts:client", version.ref = "apialerts" }
```

Then apply it in `build.gradle.kts`:

```kotlin
sourceSets {
    commonMain.dependencies {
        implementation(libs.apialerts.client)
    }
}
```

Ensure `mavenCentral()` is in your repository list in `settings.gradle.kts`.

### Manual (without version catalog)

```kotlin
// build.gradle.kts
implementation("com.apialerts:client:2.0.0")
```

```groovy
// build.gradle (Groovy)
implementation 'com.apialerts:client:2.0.0'
```

## Quick Start

```kotlin
import com.apialerts.client.ApiAlerts
import com.apialerts.client.Event

ApiAlerts.configure("your-api-key")
ApiAlerts.send(Event(message = "Deploy complete"))
```

## Usage

### Global singleton (recommended)

Call `configure` once at startup, then use `send` / `sendAsync` anywhere.

```kotlin
import com.apialerts.client.ApiAlerts
import com.apialerts.client.Event

ApiAlerts.configure("your-api-key")

// Fire-and-forget — critical errors always logged; HTTP errors logged when debug is enabled
ApiAlerts.send(Event(message = "Deploy complete"))

// Awaitable send — returns kotlin.Result<SendResult>
val result = ApiAlerts.sendAsync(Event(message = "Deploy complete"))
result.onSuccess { println("Sent to ${it.workspace} (${it.channel})") }
result.onFailure { println("Error: ${it.message}") }
```

### DSL style

```kotlin
ApiAlerts.send {
    message = "Deploy complete"
    channel = "releases"
    event   = "ci.deploy"
    title   = "Deployed"
    tags    = listOf("CI/CD", "Kotlin")
    link    = "https://github.com/apialerts/apialerts-kotlin/actions"
}
```

### Event fields

Only `message` is required. All other fields are optional.

```kotlin
import com.apialerts.client.Event
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

val event = Event(
    message = "Deploy complete",
    channel = "releases",
    event   = "ci.deploy",
    title   = "Deployed",
    tags    = listOf("CI/CD", "Kotlin"),
    link    = "https://github.com/apialerts/apialerts-kotlin/actions",
    data    = buildJsonObject { put("version", "2.0.0") },
)
```

| Field     | Type         | Required | Description                      |
|-----------|--------------|----------|----------------------------------|
| `message` | `String`     | Yes      | Main notification message        |
| `channel` | `String?`    | No       | Target channel name              |
| `event`   | `String?`    | No       | Event key (e.g. `ci.deploy`)     |
| `title`   | `String?`    | No       | Short title                      |
| `tags`    | `List<String>?` | No    | Categorisation tags              |
| `link`    | `String?`    | No       | URL attached to the notification |
| `data`    | `JsonObject?`| No       | Arbitrary key-value metadata     |

### Send to multiple workspaces

```kotlin
ApiAlerts.sendWithKeyAsync("other-api-key", Event(message = "Deploy complete"))
    .onSuccess { println("Sent to ${it.workspace} (${it.channel})") }
    .onFailure { println("Error: ${it.message}") }
```

## Java Interop

The library is fully usable from Java. Use `EventBuilder` for a refactor-safe, named-field experience:

```java
import com.apialerts.client.ApiAlerts;
import com.apialerts.client.EventBuilder;

ApiAlerts.configure("your-api-key", false);

// Fire-and-forget
ApiAlerts.send(new EventBuilder("Deploy complete").build());

// CompletableFuture — completes exceptionally with ApiAlertsException on failure
ApiAlertsJvm.sendFuture(new EventBuilder("Deploy complete").build())
    .thenAccept(result -> {
        System.out.println("Sent to " + result.getWorkspace() + " (" + result.getChannel() + ")");
    })
    .exceptionally(e -> {
        System.err.println("Error: " + e.getMessage());
        return null;
    });
```

> For Swift/Objective-C projects, use the dedicated [apialerts-swift](https://github.com/apialerts/apialerts-swift) package for a more idiomatic experience.

## Links

- [Documentation](https://apialerts.com/docs)
- [Sign up](https://apialerts.com)
- [GitHub Issues](https://github.com/apialerts/apialerts-kotlin/issues)
- [Maven Central](https://central.sonatype.com/artifact/com.apialerts/client)
