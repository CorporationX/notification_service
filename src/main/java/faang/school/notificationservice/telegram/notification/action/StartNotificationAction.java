package faang.school.notificationservice.telegram.notification.action;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.telegram.UserTelegramDto;
import faang.school.notificationservice.exception.TelegramException;
import faang.school.notificationservice.telegram.NotificationAction;
import faang.school.notificationservice.telegram.NotificationActionType;
import faang.school.notificationservice.telegram.notification.messages.StartNotificationMessage;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartNotificationAction implements NotificationAction {
    private final UserServiceClient userClient;

    @Override
    public boolean isAcceptable(NotificationActionType type) {
        return type == NotificationActionType.START;
    }

    @Override
    public String processAndReturnMessage(Message message) {
        log.info("Action: {}. Processing started. Chat id: {}", NotificationActionType.START.getAction(), message.getChatId());

        String userName = message.getChat().getUserName();
        try {
            UserTelegramDto userTelegramDto = userClient.getUserByTelegram(userName);
            if (userTelegramDto.telegramChatId() != null && userTelegramDto.telegramChatId() > 0) {
                log.info("Action: {}. Chat already connected for user {}", NotificationActionType.START.getAction(), userTelegramDto.userId());
                return StartNotificationMessage.SUCCESS_ALREADY_CONNECTED.getMessage();
            }

            UserTelegramDto updatedUserTelegramDto =
                    userClient.addUserTelegram(new UserTelegramDto(userTelegramDto.userId(), message.getChatId(), userTelegramDto.telegramUserName()));
            if (updatedUserTelegramDto.telegramChatId() == null) {
                throw new TelegramException(String.format("Failed to set chat id to user %d", userTelegramDto.userId()));
            }
        } catch (Exception e) {
            log.info("Action: {}. Failed to connect chat {}", NotificationActionType.START.getAction(), message.getChatId(), e);
            if (e instanceof FeignException && ((FeignException) e).status() == HttpStatus.NOT_FOUND.value()) {
                return StartNotificationMessage.FAILED_NEED_CONNECT.getMessage();
            }

            return StartNotificationMessage.FAILED_UNEXPECTED.getMessage();
        }

        log.info("Action: {}. Processing finished. Chat id: {}", NotificationActionType.START.getAction(), message.getChatId());
        return StartNotificationMessage.SUCCESS_CONNECTED.getMessage();
    }

    @Override
    public InlineKeyboardMarkup getMarkup() {
        List<InlineKeyboardRow> actionRows = Arrays.stream(NotificationActionType.values())
                .map(this::createKeyboardRow)
                .toList();

        return new InlineKeyboardMarkup(actionRows);
    }

    private InlineKeyboardRow createKeyboardRow(NotificationActionType type) {
        return new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                        .text(type.getAction())
                        .callbackData(type.getAction())
                        .build()
        );
    }
}
