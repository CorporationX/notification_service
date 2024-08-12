package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Slf4j
public class TgNotificationService extends TelegramLongPollingBot implements NotificationService {
    private final TelegramChatService telegramChatService;
    private final Map<Long, String> userStates = new HashMap<>();

    @Value("${spring.telegram.name}")
    private String name;
    @Value("${spring.telegram.token}")
    private String token;
    @Value("${message.ask-id}")
    private String askId;
    @Value("${message.invalid-id}")
    private String incorrectId;
    @Value("${message.already-registered}")
    private String alreadyRegistered;
    @Value("${message.stop}")
    private String stop;
    @Value("${message.stop.answer}")
    private String answerStop;
    @Value("${message.unknown-command}")
    private String unknownCommand;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equalsIgnoreCase(stop)) {
                telegramChatService.deleteChatId(chatId);
                userStates.remove(chatId);
                sendTextMessage(chatId, answerStop);
                return;
            }

            if (telegramChatService.checkTelegramChatIsAlive(chatId)) {
                sendTextMessage(chatId, alreadyRegistered);
                return;
            }

            String state = userStates.getOrDefault(chatId, "ASK_LOGIN");

            switch (state) {
                case "ASK_LOGIN":
                    sendTextMessage(chatId, askId);
                    userStates.put(chatId, "WAITING_FOR_ID");
                    break;

                case "WAITING_FOR_ID":
                    if (messageText.matches("\\d+")) {
                        Long userId = Long.valueOf(messageText);
                        sendTextMessage(chatId, telegramChatService.createChatId(chatId, userId));
                        userStates.put(chatId, "REGISTERED");
                    } else {
                        sendTextMessage(chatId, incorrectId);
                    }
                    break;

                case "REGISTERED":
                    sendTextMessage(chatId, alreadyRegistered);
                    break;

                default:
                    sendTextMessage(chatId, unknownCommand);
                    break;
            }
        }
    }

    private void sendTextMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chatId {}: {}", chatId, e.getMessage());
            throw new EntityNotFoundException("ChatId not found");
        }
    }

    @Override
    public void send(UserDto user, String message) {
        telegramChatService.
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return null;
    }
}


