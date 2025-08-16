package faang.school.notificationservice.config.properties.email;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EmailProtocolProperties {
    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String protocol;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnabled;
}
