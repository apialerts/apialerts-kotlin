# apialerts-kotlin

Kotlin/Java client for [apialerts.com](https://apialerts.com/)

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

### Send message with custom api key

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

The ApiAlerts.sendAsync methods are also available if you need to wait for a successful execution.

