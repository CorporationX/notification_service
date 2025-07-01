package faang.school.notificationservice.config.sms;

import com.vonage.client.VonageClient;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsVonageClient {
    private final VonageConfig vonageConfig;
    @Getter
    private VonageClient client;

    @PostConstruct
    public void init() {
        client = com.vonage.client.VonageClient.builder()
                .apiKey(vonageConfig.key())
                .apiSecret(vonageConfig.secret())
                .build();
    }
}
