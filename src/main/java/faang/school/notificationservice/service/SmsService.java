package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.SmsResponse;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsIntegrationException;
import faang.school.notificationservice.service.provider.SmsProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    public static final String NAME_FROM = "Test SMS from Anton B.";
    private final SmsProvider smsProvider;

    @Override
    public void send(@Valid UserDto user, @NotBlank String message) {
        SmsResponse response = smsProvider.sendSms(NAME_FROM, user.getPhone(), message);
        validateResponse(response);
    }

    @Override
    public void sendGroup(@Valid List<UserDto> users, @NotBlank String message) {
        users.forEach(user -> {
            try {
                send(user, message);
            } catch (SmsIntegrationException e) {
                log.error("Failed to send SMS to {}: {}", user.getPhone(), e.getMessage());
            }
        });
    }

    private void validateResponse(SmsResponse smsResponse) {
        if (!smsResponse.isSuccess()) {
            String errorDetails = smsResponse.getErrorMessage() != null ?
                    smsResponse.getErrorMessage() : "No error details provided";
            log.error("SMS delivery failed. Reason: {}", errorDetails);
            throw new SmsIntegrationException("Failed to send SMS: " + errorDetails);
        }
        log.info("SMS sent successfully. Provider ID: {}", smsResponse.getProviderId());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;

    }
}
