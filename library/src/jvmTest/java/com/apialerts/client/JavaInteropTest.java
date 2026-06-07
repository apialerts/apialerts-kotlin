package com.apialerts.client;

import org.junit.Test;
import java.util.concurrent.CompletableFuture;
import static org.junit.Assert.assertNotNull;

// Verifies the Java-facing surface (static ApiAlerts, EventBuilder, the
// CompletableFuture bridge) is callable from Java. Java rides on the same
// Maven artifact, and none of this is exercised by the Kotlin tests.
public class JavaInteropTest {

    @Test
    public void javaCanConfigureBuildAndSend() {
        ApiAlerts.configure("test-key");
        ApiAlerts.setDebug(false);
        ApiAlerts.setOverrides("apialerts-java", "1.1.0", "https://example.com/event");

        Event event = new EventBuilder("Java interop")
            .channel("testing")
            .event("java.interop")
            .title("Java")
            .build();

        // Static fire-and-forget (no key override, and with override)
        ApiAlerts.send(event);
        ApiAlerts.send(event, "other-key");

        // CompletableFuture bridge for awaitable delivery
        CompletableFuture<SendResult> future = ApiAlertsJvm.sendFuture(event);
        assertNotNull(future);
    }

    @Test
    public void javaCanUseTheInjectableInstanceClient() {
        // The DI path: construct, inject, send (Spring would hold this as a bean).
        ApiAlertsClient client = ApiAlertsJvm.client("test-key");
        assertNotNull(client);

        Event event = new EventBuilder("Java DI").channel("testing").build();

        // Fire-and-forget on the instance
        client.send(event, null);

        // Awaitable on the instance (sendAsync is suspend, so use the bridge)
        CompletableFuture<SendResult> future = ApiAlertsJvm.sendFuture(client, event);
        assertNotNull(future);
    }
}
