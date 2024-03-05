package com.apialerts.sample

import com.apialerts.kotlin.client.ApiAlerts
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val client = ApiAlerts

    client.configure(
        apiKey = "f1a9ba4d-8b23-413a-8747-a3c03946be7b",
        logging = true
    )

    client.send(
        message = "First integration of apialerts-kotlin",
        tags = listOf("integration", "kotlin"),
        link = "https://github.com/apialerts/apialerts-kotlin"
    )
//    client.send(
//        apiKey = "KEY_2",
//        message = "overridden key"
//    )
//
//    client.send(
//        message = "default key"
//    )
//
//    ApiAlerts.send(
//        message = "new client"
//    )

    delay(10000)
}