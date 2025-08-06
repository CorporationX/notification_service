package faang.school.notificationservice.service;

import faang.school.notificationservice.client.ProstorSmsClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MessageSendException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProstorSmsService implements SmsService {
    private final ProstorSmsClient client;

    @Override
    public void send(UserDto user, String message) {
        log.info("Sending SMS to phone={} text='{}'", user.getPhone(), message);
        String response = client.sendMessage(user.getPhone(), message);
        handleResponse(response);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

    @Override
    public boolean isSupportRussianPhone() {
        return true;
    }

    private void handleResponse(String response) {
        String[] responseArgs = response.split(";");
        String messageStatus = responseArgs[0];
        if (!MessageStatus.ACCEPTED.getName().equals(messageStatus)) {
            String errorMessage = MessageStatus.getErrorMessageByStatus(responseArgs[1]);
            throw new MessageSendException("SMS sending failed. Cause: {}", errorMessage);
        }
        String messageId = responseArgs[1];
        log.info("SMS id={} sent successfully", messageId);
    }

    @RequiredArgsConstructor
    private enum MessageStatus {
        ACCEPTED("accepted", "Сообщение принято сервисом"),
        INVALID_MOBILE_PHONE("invalid mobile phone", "Неверно задан номер тефона (формат +71234567890)"),
        TEXT_IS_EMPTY("text is empty", "Отсутствует текст"),
        SENDER_ADDRESS_INVALID("sender address invalid", "Неверная (незарегистрированная) подпись отправителя"),
        WAPURL_INVALID("wapurl invalid", "Неправильный формат wap-push ссылки"),
        INVALID_SCHEDULE_TIME("invalid schedule time format", "Неверный формат даты отложенной отправки сообщения"),
        INVALID_STATUS_QUEUE("invalid status queue name", "Неверное название очереди статусов сообщений"),
        NOT_ENOUGH_BALANCE("not enough balance", "Баланс пуст (проверьте баланс)"),
        UNKNOWN_ERROR("unknown error", "Неизвестная ошибка");

        @Getter
        private final String name;
        private final String errorMessage;

        public static String getErrorMessageByStatus(String currentStatus) {
            return Arrays.stream(values())
                    .filter(status -> status.name.equals(currentStatus))
                    .findFirst()
                    .orElse(UNKNOWN_ERROR)
                    .errorMessage;
        }
    }
}
