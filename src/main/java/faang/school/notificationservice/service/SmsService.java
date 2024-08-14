package faang.school.notificationservice.service;


import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ExceptionMessages;
import faang.school.notificationservice.exception.notification.SmsSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    @Value("${twilio.phone-number}")
    private String sender;
    private final TwilioRestClient twilioRestClient;

    @Override
    public void send(UserDto user, String message) {
        validateUserPhoneNumber(user);
        try {
            var messageSent = Message.creator(
                    new PhoneNumber(user.getPhone()),
                    new PhoneNumber(sender),
                    message
            ).create(twilioRestClient);
            log.info("SMS sent to user with id {}. The contents: {}", user.getId(), messageSent);
        } catch (Exception e) {
            log.error("Failed to send SMS to user: {}", user.getId(), e);
            throw new SmsSendingException(String.format(ExceptionMessages.SMS_SENDING_FAILURE, user.getId()), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    private void validateUserPhoneNumber(UserDto user) {
        var phoneNumber = user.getPhone();
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            log.error("Unable to send sms. Invalid phoneNumber number: {}", phoneNumber);
            throw new SmsSendingException(String.format(ExceptionMessages.INVALID_PHONE_NUMBER, user.getId()));
        }
    }
}
