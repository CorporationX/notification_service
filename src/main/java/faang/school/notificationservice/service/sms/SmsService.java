package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.config.properties.VonageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.InvalidMessageException;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.validation.UserSmsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.PHONE;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {

    private final SmsGateway gateway;
    private final VonageProperties props;
    private final UserSmsValidator userSmsValidator;

    @Override
    public void send(UserDto user, String message) {
        userSmsValidator.validateForSms(user);
        validateMessage(message);

        String recipientPhoneNumber = user.getPhone();
        log.info("Triggering SMS send for user {} with phone {}", user.getId(), recipientPhoneNumber);

        gateway.send(props.from(), recipientPhoneNumber, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return PHONE;
    }

    private void validateMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new InvalidMessageException("SMS message must not be blank");
        }
    }
}
