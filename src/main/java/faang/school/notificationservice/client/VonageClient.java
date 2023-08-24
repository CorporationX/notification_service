package faang.school.notificationservice.client;

import io.lettuce.core.dynamic.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VonageClient {
    @Value("${vonage.api.key}")
    private String apiKey;
    @Value("${vonage.api-secret}")
    private String apiSecret;
    @Value("${vonage.from}")
    private String from;

//? надо ли писать from - нужно выносить в йамл

    public VonageClient(String apiKey, String apiSecret, String from) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.from = from;
    }

/*  @Bean
    @PostConstruct
    public static com.vonage.client.VonageClient createVonageClient() {
        return com.vonage.client.VonageClient.builder()
                .apiKey("ec1a2ee7")
                .apiSecret("HPRDmtd9nEOsdhqh")
                .build();
    }*/
}
