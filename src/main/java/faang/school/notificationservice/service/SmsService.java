package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.vonage.VonageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error.SmsSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;
    private final VonageProperties props;

    private static final Map<String, String> COUNTRY_CODES = Map.of(
            "SG", "+65",
            "US", "+1",
            "GB", "+44",
            "IN", "+91",
            "CN", "+86",
            "RU", "+7"
    );

    @Override
    @Retryable(
            value = {SmsSendException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 2.0)
    )
    public void send(UserDto user, String message) {
        if (!props.isEnabled()) {
            log.info("Vonage disabled. Skip SMS to {}: {}", user.getId(), message);
            return;
        }
        String to = normalizedPhone(user.getPhone(), props.getDefaultCountry());
        TextMessage textMessage = new TextMessage(props.getFrom(), to, message);

        try {
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
            SmsSubmissionResponseMessage first = response.getMessages().get(0);
            log.info("SMS to {}: {}. Status: {}", user.getId(), message, first.getStatus());
            if (first.getStatus() != MessageStatus.OK) {
                String err = first.getErrorText();
                log.warn("SMS not sent. status={}, error={}", first.getStatus(), err);
                throw new SmsSendException("Vonage rejected SMS: " + err);
            }
            log.info("SMS sent to {} (message-id={})", user.getId(), first.getId());

        } catch (SmsSendException e) {
            log.warn("SMS send failed (domain): userId={}, reason={}", user.getId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("SMS send failed to {}: {}", to, e.getMessage(), e);
            throw new SmsSendException("SMS send failed", e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

    private String normalizedPhone(String raw, String defaultCountry) {
        if (raw == null || raw.isBlank()) {
            throw new SmsSendException("User phone is empty");
        }
        String trimmed = raw.replaceAll("\\s+", "");
        if (trimmed.startsWith("+")) return trimmed;
        String countryCode = COUNTRY_CODES.getOrDefault(defaultCountry, "+7");
        return countryCode + trimmed;
    }
}
