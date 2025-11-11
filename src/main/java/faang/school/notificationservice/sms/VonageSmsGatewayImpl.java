package faang.school.notificationservice.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.vonage.VonageProperties;
import faang.school.notificationservice.error.SmsGatewayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VonageSmsGatewayImpl implements SmsGateway {

    private final VonageClient vonageClient;
    private final VonageProperties props;

    @Override
    public void send(String to, String text) {

        if (!props.isEnabled()) {
            log.info("Vonage disabled. Skip SMS to {}: {}", to, text);
            return;
        } else {
            log.info("Vonage enabled. Sending SMS to {}: {}", to, text);
        }
        to = normalizePhone(to, props.getDefaultCountry());
        TextMessage msg = new TextMessage(props.getFrom(), to, text);
        try {
            SmsSubmissionResponse resp = vonageClient.getSmsClient().submitMessage(msg);
            SmsSubmissionResponseMessage m = resp.getMessages().get(0);
            if (m.getStatus() != MessageStatus.OK) {
                String err = m.getErrorText();
                log.warn("SMS rejected. status={}, error={}", m.getStatus(), err);
                throw new SmsGatewayException("Vonage rejected SMS: " + err);
            }
            log.info("SMS sent to {} (message-id={})", to, m.getId());
        } catch (SmsGatewayException e) {
            log.error("SMS send failed to {}: {}", to, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("SMS send failed to {}: {}", to, e.getMessage(), e);
            throw new SmsGatewayException("SMS send failed", e);
        }
    }

    private String normalizePhone(String raw, String defaultCountry) {
        if (raw == null || raw.isBlank()) {
            throw new SmsGatewayException("User phone is empty");
        }
        String trimmed = raw.replaceAll("\\s+", "");
        if (trimmed.startsWith("+")) return trimmed;
        String countryCode = props.getCountryCodes().getOrDefault(defaultCountry, "+7");
        return countryCode + trimmed;
    }
}