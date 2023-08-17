package faang.school.notificationservice.client;

import com.vonage.client.VonageClient;

public class VonageInitializer {
    public static VonageClient createVonageClient() {
        return VonageClient.builder()
                .apiKey("ec1a2ee7")
                .apiSecret("HPRDmtd9nEOsdhqh")
                .build();
    }
}
