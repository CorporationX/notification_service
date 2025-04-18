package faang.school.notificationservice.service;

import faang.school.notificationservice.config.telegram.TelegramConfig;
import faang.school.notificationservice.service.telegram.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramBotTest {

    @Mock
    private TelegramConfig telegramConfig;

    @Spy
    @InjectMocks
    private TelegramBot telegramBot;

    private final long CHAT_ID = 12345L;
    private final String MESSAGE_TEXT = "Test message";
    private Message message;
    private SendMessage sendMessage;

    @BeforeEach
    void setUp() {
        message = new Message();
        sendMessage = new SendMessage();
    }

    @Test
    @DisplayName("Проверка успешного отправления сообщения")
    public void givenMessage_whenSendMessage_thenSuccess() throws TelegramApiException {
        doReturn(message).when(telegramBot).execute(any(SendMessage.class));

        telegramBot.sendMessage(CHAT_ID, MESSAGE_TEXT);

        verify(telegramBot, times(1)).execute(argThat((SendMessage message) ->
                message.getChatId().equals(String.valueOf(CHAT_ID)) &&
                        message.getText().equals(MESSAGE_TEXT)
        ));
    }
}