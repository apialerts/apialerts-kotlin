package com.apialerts.kotlin.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

class JvmPlatform : Platform {
    override val name: String = "JVM"

    override fun getHttpClient(): HttpClientEngine {
        return CIO.create {
            this.requestTimeout = 60000
        }
    }
}

actual fun getPlatform(): Platform = JvmPlatform()