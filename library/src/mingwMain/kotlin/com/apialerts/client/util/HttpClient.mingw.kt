package com.apialerts.client.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.winhttp.WinHttp

internal actual fun createHttpClient(): HttpClient =
    HttpClient(WinHttp) { configureApiAlerts() }
