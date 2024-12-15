# apialerts-kotlin

Kotlin/Java client for the [apialerts.com](https://apialerts.com/) platform

[Docs](https://apialerts.com/docs/kotlin) • [GitHub](https://github.com/apialerts/apialerts-kotlin) • [Maven](https://central.sonatype.com/artifact/com.apialerts/client/overview)

## Installation

Add the following dependency to your build.gradle

```groovy
dependencies {
  implementation 'com.apialerts:client:<latest-version>'
}
```

or toml file:

```toml
apialerts = { module = "com.apialerts:client", version = "<latest-version>" }
```

Ensure `mavenCentral()` is added to your repository list


### Initialize the client

```kotlin
// Custom config can be passed to the client
ApiAlerts.configure(
    apiKey = "your-api-key",
    debug = true
)
```

### Send Events

You can send alerts using the simple style or DSL style.

Simple Style
```kotlin
ApiAlerts.send(
  apiKey = "your-api-key",        // Optional, uses the key from ApiAlerts.configure() if not provided
  channel = "your-channel",       // Optional, uses the default channel if not provided
  message = "Your alert message",
  tags = listOf("tag1", "tag2"),  // Optional tags
  link = "https://example.com"    // Optional link
)
```

DSL Style
```kotlin
ApiAlerts.send {
  apiKey = "your-api-key"         // Optional, uses the key from ApiAlerts.configure() if not provided
  channel = "your-channel"        // Optional, uses the default channel if not provided
  message = "Your alert message"
  tags = listOf("tag1", "tag2")   // Optional tags
  link = "https://example.com"    // Optional link
}
```

The ApiAlerts.sendAsync methods are also available if you need to wait for a successful execution. However, the send() functions are generally always preferred.

### Feedback & Support

If you have any questions or feedback, please create an issue on our GitHub repository. We are always looking to improve our service and would love to hear from you. Thanks for using API Alerts!

