package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.config.properties.VonageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsValidationException;
import faang.school.notificationservice.service.NotificationService;
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

    @Override
    public void send(UserDto user, String message) {
        validate(user, message);

        String recipientPhoneNumber = user.getPhone();
        log.info("Triggering SMS send for user: {}", recipientPhoneNumber);
        gateway.send(props.from(), recipientPhoneNumber, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return PHONE;
    }

    private void validate(UserDto user, String message) {
        if (user == null) {
            throw new SmsValidationException("User must not be null");
        }
        if (user.getPhone() == null || user.getPhone().isBlank()) {
            throw new SmsValidationException("Phone number is missing");
        }
        if (message == null || message.isBlank()) {
            throw new SmsValidationException("Message is missing");
        }
    }
}
