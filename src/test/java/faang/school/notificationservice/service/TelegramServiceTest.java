package faang.school.notificationservice.service;

import faang.school.notificationservice.service.telegram.TelegramBot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {
    @Mock
    private TelegramBot telegramBot;

    @Test
    public void shouldSendMessage() throws TelegramApiException {
        long chatId = 1;
        String message = "You're subscribed!";
        Message desiredMessage = new Message();
        desiredMessage.setText(message);

        SendMessage messageToTelegram = new SendMessage();
        messageToTelegram.setChatId(chatId);
        messageToTelegram.setText(message);

        Mockito.when(telegramBot.execute(messageToTelegram)).thenReturn(desiredMessage);
        Message receivedMessage = telegramBot.execute(messageToTelegram);

        Assertions.assertEquals(messageToTelegram.getText(), receivedMessage.getText());
        Mockito.verify(telegramBot, Mockito.times(1)).execute(messageToTelegram);
    }
}
