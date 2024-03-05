package com.apialerts.kotlin.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import java.util.concurrent.TimeUnit

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    override fun getHttpClient(): HttpClientEngine {
        return OkHttp.create {
            config {
                callTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                connectTimeout(60, TimeUnit.SECONDS)
            }
        }
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()