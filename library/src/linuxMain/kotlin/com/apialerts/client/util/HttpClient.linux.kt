package com.apialerts.client.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.curl.Curl

internal actual fun createHttpClient(): HttpClient =
    HttpClient(Curl) { configureApiAlerts() }
