# API Alerts • Kotlin Multiplatform Client

[![Maven Central](https://img.shields.io/maven-central/v/com.apialerts/client)](https://central.sonatype.com/artifact/com.apialerts/client)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[Maven Central](https://central.sonatype.com/artifact/com.apialerts/client) • [GitHub](https://github.com/apialerts/apialerts-kotlin) • [API Alerts](https://apialerts.com)

Effortless project notifications. Send once, deliver everywhere.

Kotlin Multiplatform library covering Android, JVM (Java + Kotlin), Apple (iOS/macOS/watchOS/tvOS), JavaScript, WebAssembly, Linux, and Windows native from a single dependency. HTTP is handled by Ktor across every target.

## Supported targets

| Group              | Targets                                                                                                                   |
|--------------------|---------------------------------------------------------------------------------------------------------------------------|
| JVM                | `androidLibrary`, `jvm` (Java 11+)                                                                                        |
| Apple [^apple-x64] | `iosArm64`, `iosSimulatorArm64`, `macosArm64`, `watchosArm64`, `watchosSimulatorArm64`, `tvosArm64`, `tvosSimulatorArm64` |
| Web                | `js` (browser + Node.js), `wasmJs` (browser + Node.js)                                                                    |
| Native             | `linuxX64`, `linuxArm64`, `mingwX64`                                                                                      |

## Installation

Add the dependency to your `libs.versions.toml`:

```toml
[versions]
apialerts = "1.1.0"

[libraries]
apialerts-client = { module = "com.apialerts:client", version.ref = "apialerts" }
```

Apply in `build.gradle.kts`:

```kotlin
sourceSets {
    commonMain.dependencies {
        implementation(libs.apialerts.client)
    }
}
```

Ensure `mavenCentral()` is in your repository list.

### Manual (without version catalog)

```kotlin
implementation("com.apialerts:client:1.1.0")
```

## Quick Start

```kotlin
import com.apialerts.client.ApiAlerts
import com.apialerts.client.Event

ApiAlerts.configure("your-api-key")
ApiAlerts.send(Event(message = "Deploy complete"))
```

## Usage

### Global singleton

Call `configure` once at startup, then use `send` / `sendAsync` anywhere.

```kotlin
import com.apialerts.client.ApiAlerts
import com.apialerts.client.Event

ApiAlerts.configure("your-api-key")

// Fire-and-forget. Critical errors always logged; HTTP errors only when debug is enabled.
ApiAlerts.send(Event(message = "Deploy complete"))

// Awaitable. Returns kotlin.Result<SendResult>; never throws.
val result = ApiAlerts.sendAsync(Event(message = "Deploy complete"))
result.onSuccess { sent -> println("Sent to ${sent.workspace} (${sent.channel})") }
result.onFailure { e -> println("Error: ${e.message}") }
```

### DSL form

`send { ... }` / `sendAsync { ... }` accept a builder lambda:

```kotlin
ApiAlerts.send {
    message = "Deploy complete"
    channel = "releases"
    event   = "ci.deploy.success"
    title   = "Deployed"
    tags    = listOf("CI/CD", "Kotlin")
    link    = "https://github.com/apialerts/apialerts-kotlin/actions"
}
```

### Dependency injection

Prefer an injected client over the global singleton when you use DI, mock in tests, or need multiple keys. Construct an `ApiAlertsClient` and inject it:

```kotlin
import com.apialerts.client.ApiAlertsClient

// Koin
single<ApiAlertsClient> { ApiAlertsClient(apiKey = "your-api-key") }

// anywhere it's injected
class DeployNotifier(private val alerts: ApiAlertsClient) {
    suspend fun onDeploy() = alerts.sendAsync { message = "Deploy complete" }
}
```

`ApiAlertsClient` exposes the same `send` / `sendAsync` (plus the DSL) as the singleton - `ApiAlerts` is a thin facade over a default instance. Use the singleton for quick one-off use, the instance for DI.

### Enable debug logging

```kotlin
ApiAlerts.configure("your-api-key", debug = true)
```

The SDK uses [kermit](https://github.com/touchlab/Kermit) under the tag `apialerts`. Critical errors (missing API key, not yet configured) always log. Success / warning / non-critical errors log only when `debug = true`.

### Event fields

Only `message` is required. All other fields are optional and omitted from the request body when `null`.

```kotlin
import com.apialerts.client.Event
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

val event = Event(
    message = "Deploy complete",
    channel = "releases",
    event   = "ci.deploy.success",
    title   = "Deployed",
    tags    = listOf("CI/CD", "Kotlin"),
    link    = "https://github.com/apialerts/apialerts-kotlin/actions",
    data    = buildJsonObject { put("version", "1.1.0") },
)
```

| Field     | Type            | Required | Description                                                                                                                                                                                                           |
|-----------|-----------------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `message` | `String`        | Yes      | Human-readable notification text. This is what appears on the push notification lock screen.                                                                                                                          |
| `channel` | `String?`       | No       | Workspace channel the push notification fires on. Defaults to the workspace default channel when omitted.                                                                                                             |
| `event`   | `String?`       | No       | Identifies what kind of thing happened. Optional but recommended. Use dotted notation (e.g. `ci.deploy.success`, `payment.failed`, `user.signup`) so routing rules can match glob patterns like `ci.*` or `*.failed`. |
| `title`   | `String?`       | No       | Short headline some destinations render separately from the message body.                                                                                                                                             |
| `tags`    | `List<String>?` | No       | Categorisation tags for filtering and search.                                                                                                                                                                         |
| `link`    | `String?`       | No       | URL associated with the event. Available as a deeplink for push notifications and as a call-to-action for routed destinations.                                                                                        |
| `data`    | `JsonObject?`   | No       | Arbitrary key-value metadata. Available to non-push destinations for templating (Slack message bodies, email templates, webhook payloads).                                                                            |

### Send to multiple workspaces

Pass an `apiKey` as the optional second argument to override the configured key for a single call.

```kotlin
ApiAlerts.send(Event(message = "Deploy complete"), apiKey = "other-workspace-api-key")

val result = ApiAlerts.sendAsync(
    Event(message = "Deploy complete"),
    apiKey = "other-workspace-api-key",
)
```

## API

| Method                                                  | Description                                                                 |
|---------------------------------------------------------|-----------------------------------------------------------------------------|
| `ApiAlerts.configure(apiKey, debug = false)`            | Initialise the singleton. First call wins; subsequent calls are no-ops.     |
| `ApiAlerts.setOverrides(integration, version, baseUrl)` | For wrapper libraries to identify themselves in the `X-Integration` header. |
| `ApiAlerts.send(event, apiKey = null)`                  | Fire-and-forget. Never throws; drops errors silently unless `debug` is on.  |
| `ApiAlerts.send { ... }`                                | Fire-and-forget DSL form.                                                   |
| `ApiAlerts.sendAsync(event, apiKey = null)`             | Awaitable, returns `Result<SendResult>`. Never throws.                      |
| `ApiAlerts.sendAsync { ... }`                           | Awaitable DSL form.                                                         |
| `ApiAlertsClient(apiKey, debug = false)`                | Construct an injectable instance. Same `send` / `sendAsync` (+ DSL) as the singleton. |

### SendResult fields

| Field       | Type           | Description                         |
|-------------|----------------|-------------------------------------|
| `workspace` | `String?`      | Workspace name (present on success) |
| `channel`   | `String?`      | Channel name (present on success)   |
| `warnings`  | `List<String>` | Non-fatal server warnings           |

Errors are surfaced via `Result.failure(ApiAlertsException(message))`, not as fields on `SendResult`.

## Java interop

The library is fully usable from Java. See the [Java README](#) for details, or jump straight in:

```java
import com.apialerts.client.ApiAlerts;
import com.apialerts.client.ApiAlertsJvm;
import com.apialerts.client.EventBuilder;

ApiAlerts.configure("your-api-key");

// Fire-and-forget
ApiAlerts.send(new EventBuilder("Deploy complete").build());

// Awaitable via CompletableFuture - completes exceptionally with ApiAlertsException on failure
ApiAlertsJvm.sendFuture(new EventBuilder("Deploy complete").build())
    .thenAccept(result -> System.out.println("Sent to " + result.getWorkspace() + " (" + result.getChannel() + ")"))
    .exceptionally(e -> { System.err.println("Error: " + e.getMessage()); return null; });
```

For dependency injection (Spring), construct an injectable client with `ApiAlertsJvm.client(...)`:

```java
import com.apialerts.client.ApiAlertsClient;

ApiAlertsClient client = ApiAlertsJvm.client("your-api-key");
ApiAlertsJvm.sendFuture(client, new EventBuilder("Deploy complete").build());
```

> For Swift / Objective-C projects, use [apialerts-swift](https://github.com/apialerts/apialerts-swift) instead - more idiomatic Swift surface than this KMP artifact.

## Links

- [Kotlin documentation](https://apialerts.com/docs/sdks/kotlin)
- [Java documentation](https://apialerts.com/docs/sdks/java)
- [Sign up](https://apialerts.com)
- [GitHub Issues](https://github.com/apialerts/apialerts-kotlin/issues)
- [Maven Central](https://central.sonatype.com/artifact/com.apialerts/client)

[^apple-x64]: Apple Silicon targets only. `iosX64`, `macosX64`, `watchosX64`, and `tvosX64` are intentionally not published. JetBrains has deprecated the `*X64` Apple Kotlin/Native targets, all Apple hardware sold since 2020 is Apple Silicon, and maintaining x86_64 builds is non-trivial CI cost for very few real users. Simulators still work on Apple Silicon via `iosSimulatorArm64` / `watchosSimulatorArm64` / `tvosSimulatorArm64` (the simulator on an Apple Silicon Mac runs as arm64). You'll only feel the gap if you develop on an Intel Mac and run the iOS Simulator locally, or ship a macOS app as a Universal binary for Intel Mac end-users. Device builds (`iosArm64`), App Store releases, and all Apple Silicon dev workflows are fully supported. If you hit one of those two cases, open an issue and we'll revisit.
