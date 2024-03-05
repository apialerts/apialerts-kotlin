package com.apialerts.kotlin.client

import io.ktor.client.engine.HttpClientEngine

interface Platform {
    val name: String
    fun getHttpClient(): HttpClientEngine
}

expect fun getPlatform(): Platform