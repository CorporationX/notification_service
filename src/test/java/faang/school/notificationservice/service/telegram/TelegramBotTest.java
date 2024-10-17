package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.telegram.TelegramBotProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    @Spy
    private TelegramBotProperties properties;

    @Spy
    @InjectMocks
    private TelegramBot telegramBot;

    @Mock
    private Message message;

    @Mock
    private Update update;

    @Captor
    private ArgumentCaptor<SendSticker> sendStickerArgumentCaptor;

    private String defaultStickerId;
    private String testBot;

    @BeforeEach
    void setUp() {
        defaultStickerId = "default-sticker-id";
        testBot = "TestBot";
        properties.setName(testBot);
        properties.setToken("token");
        properties.setDefaultStickerId(defaultStickerId);
    }

    @Test
    void testOnUpdateReceived_SendsSticker() {
        long chatId = 123456789L;
        SendSticker correctResult = SendSticker.builder()
                .chatId(chatId)
                .sticker(new InputFile(defaultStickerId))
                .build();
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);

        telegramBot.onUpdateReceived(update);

        verify(telegramBot).executeAsync(sendStickerArgumentCaptor.capture());
        SendSticker result = sendStickerArgumentCaptor.getValue();
        assertEquals(correctResult, result);
    }

    @Test
    void testOnUpdateReceived_NoMessage() {
        when(update.hasMessage()).thenReturn(false);

        telegramBot.onUpdateReceived(update);

        verify(telegramBot, never()).executeAsync(any(SendSticker.class));
    }

    @Test
    void testGetBotUsername() {
        assertEquals(testBot, telegramBot.getBotUsername());
    }
}
