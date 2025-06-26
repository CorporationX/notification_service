package faang.school.notificationservice.telegram.notification;

import faang.school.notificationservice.telegram.ActionExecutor;
import faang.school.notificationservice.telegram.NotificationAction;
import faang.school.notificationservice.telegram.NotificationActionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationBotUpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final ActionExecutor actionExecutor;
    private final TelegramClient telegramClient;

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            processMessage(update.getMessage(), update.getMessage().getText());
        } else if (update.hasCallbackQuery() && (update.getCallbackQuery().getMessage() instanceof Message)) {
            processMessage((Message) update.getCallbackQuery().getMessage(), update.getCallbackQuery().getData());
        }
    }

    private void processMessage(Message message, String text) {
        Long chatId = message.getChatId();
        log.info("Message received \"{}\" from chat {}", text, chatId);

        try {
            NotificationActionType actionType = NotificationActionType.fromString(text);
            NotificationAction action = actionExecutor.getAction(actionType);
            String answerMessage = action.processAndReturnMessage(message);
            SendMessage sendMessage = SendMessage.builder()
                    .text(answerMessage)
                    .chatId(chatId)
                    .replyMarkup(action.getMarkup())
                    .build();

            sendMessage(sendMessage);
        } catch (Exception e) {
            log.error("Failed to process action \"{}\" for chat {}", message.getText(), message.getChatId(), e);
            sendMessage(SendMessage.builder()
                    .text("Action not supported")
                    .chatId(chatId)
                    .build());
        }
    }

    private void sendMessage(SendMessage message) {
        try {
            telegramClient.execute(message);
            log.info("Answer to message \"{}\" sent to chat {}", message.getText(), message.getChatId());
        } catch (TelegramApiException e) {
            log.error("Fatal error on sending answer for telegram message \"{}\" to chat {}",
                    message.getText(), message.getChatId(), e);
        }
    }
}
