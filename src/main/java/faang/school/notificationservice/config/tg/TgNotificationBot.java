package faang.school.notificationservice.config.tg;

import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.model.State;
import faang.school.notificationservice.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TgNotificationBot extends TelegramLongPollingBot {
    private final TelegramChatService telegramChatService;
    private final TgSampleProperties tgSampleProperties;
    private final Map<Long, State> userStates = new HashMap<>();

    @Value("${spring.telegram.name}")
    private String name;

    @Value("${spring.telegram.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equalsIgnoreCase(tgSampleProperties.getStop())) {
                telegramChatService.deleteChatId(chatId);
                userStates.remove(chatId);
                sendTextMessage(chatId, tgSampleProperties.getAnswerStop());
                return;
            }

            if (telegramChatService.checkTelegramChatIsAlive(chatId)) {
                sendTextMessage(chatId, tgSampleProperties.getAlreadyRegistered());
                return;
            }

            State state = userStates.getOrDefault(chatId, State.ASK_LOGIN);

            switch (state) {
                case ASK_LOGIN:
                    handleAskLogin(chatId);
                    break;

                case WAITING_FOR_ID:
                    handleWaitingForId(chatId, messageText);
                    break;

                case REGISTERED:
                    handleRegistered(chatId);
                    break;

                default:
                    handleUnknownCommand(chatId);
                    break;
            }
        }
    }

    public void sendTextMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new EntityNotFoundException("Failed to send message to chatId " + chatId);
        }
    }

    private void handleAskLogin(Long chatId) {
        sendTextMessage(chatId, tgSampleProperties.getAskId());
        userStates.put(chatId, State.WAITING_FOR_ID);
    }

    private void handleWaitingForId(Long chatId, String messageText) {
        if (messageText.matches("\\d+")) {
            Long userId = Long.valueOf(messageText);
            sendTextMessage(chatId, telegramChatService.createChatId(chatId, userId));
            userStates.put(chatId, State.REGISTERED);
        } else {
            sendTextMessage(chatId, tgSampleProperties.getIncorrectId());
        }
    }

    private void handleRegistered(Long chatId) {
        sendTextMessage(chatId, tgSampleProperties.getAlreadyRegistered());
    }

    private void handleUnknownCommand(Long chatId) {
        sendTextMessage(chatId, tgSampleProperties.getUnknownCommand());
    }
}


