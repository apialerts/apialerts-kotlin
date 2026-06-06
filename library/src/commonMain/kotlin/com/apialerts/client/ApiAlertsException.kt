package com.apialerts.client

/** Thrown by [ApiAlerts.sendAsync] (and the `ApiAlertsJvm.sendFuture` Java helper) when delivery fails. */
class ApiAlertsException(message: String) : Exception(message)
