package com.apialerts.client

/** Thrown by [ApiAlerts.sendAsync] and [ApiAlerts.sendWithKeyAsync] when delivery fails. */
class ApiAlertsException(message: String) : Exception(message)
