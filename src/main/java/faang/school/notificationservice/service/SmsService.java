package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Override
    public void send(UserDto user, String message) {
        validateUser(user);
        validateMessage(message);

        SmsSubmissionResponse response = sendSms(user, message);
        validateResponse(response);
    }

    private void validateUser(UserDto user) {
        if (user == null || user.getPhone() == null || user.getPhone().isBlank()) {
            log.warn("Номер телефона не может быть пустым");
            throw new IllegalArgumentException("Для отправки SMS номер телефона - обязательное условие");
        }
    }

    private void validateMessage(String message) {
        if (message == null || message.isBlank()) {
            log.warn("Сообщение для SMS не может быть пустым");
            throw new IllegalArgumentException("Текст сообщения обязателен");
        }
    }

    private SmsSubmissionResponse sendSms(UserDto user, String message) {
        String sender = "Vonage SMS: TheStral_Stream_8";
        TextMessage textMessage = new TextMessage(sender, user.getPhone(), message);

        try {
            return vonageClient.getSmsClient().submitMessage(textMessage);
        } catch (Exception e) {
            log.error("Ошибка при отправке СМС через Vonage", e);
            throw new SmsIntegrationException("Ошибка при отправке SMS", e);
        }
    }

    private void validateResponse(SmsSubmissionResponse response) {
        if (response == null || response.getMessages() == null || response.getMessages().isEmpty()) {
            log.error("Ошибка: пустой или некорректный ответ от SMS-сервиса");
            throw new SmsIntegrationException("Некорректный ответ Vonage");
        }

        SmsSubmissionResponseMessage messageResponse = response.getMessages().get(0);
        if (messageResponse.getStatus() == MessageStatus.OK) {
            log.info("Сообщение успешно отправлено");
        } else {
            log.error("Сообщение завершилось ошибкой: {}", messageResponse.getErrorText());
            throw new SmsIntegrationException("Ошибка SMS: " + messageResponse.getErrorText());
        }
    }
    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}