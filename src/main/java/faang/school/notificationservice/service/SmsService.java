package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {
    private final VonageClient vonageClient;

    @Value("${vonage.api.sender}")
    private String sender;

    @Override
    public void send(UserDto userDto, String message) {
        String phoneNumber = userDto.getPhone();
        validatePhoneNumber(userDto);

        TextMessage sms = new TextMessage(
                sender,
                phoneNumber,
                message);

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(sms);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Сообщение отправлено на номер телефона {}.", phoneNumber);
        } else {
            log.error("Ошибка отправки сообщения: " + response.getMessages().get(0).getErrorText());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

    private void validatePhoneNumber(UserDto userDto) {
        String phoneNumber = userDto.getPhone();
        String digits = phoneNumber.substring(1);
        if (phoneNumber.isBlank()) {
            throw new DataValidationException("Телефон не может быть пустым");
        }
        if (digits.length() < 11 || digits.length() > 15) {
            throw new DataValidationException("Некорректный номер телефона {}", phoneNumber);
        }
    }
}
