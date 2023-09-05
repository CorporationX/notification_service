package faang.school.notificationservice.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.bot.BotConfig;
import faang.school.notificationservice.dto.ExtendedContactDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationTelegramBotTest {
    @Mock
    BotConfig botConfig;
    @Mock
    UserServiceClient userServiceClient;
    @Spy
    @InjectMocks
    NotificationTelegramBot notificationTelegramBot;

    @Test
    void testTextUpdate_shouldGetMessageAndButton1() throws TelegramApiException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Long chatId = 123L;
        String text = "Push the button for send you phone";
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

        SendMessage expected = new SendMessage();
        expected.setChatId(chatId);
        expected.setText(text);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().hasText()).thenReturn(true);
        when(update.getMessage().getChatId()).thenReturn(chatId);

        doThrow(TelegramApiException.class).when(notificationTelegramBot).execute(any(SendMessage.class));
        notificationTelegramBot.onUpdateReceived(update);

        verify(notificationTelegramBot).execute(captor.capture());

        SendMessage actual = captor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getText()).isEqualTo(expected.getText());
        assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        assertThat(actual.getReplyMarkup()).isNotNull();
    }

    @Test
    void testUpdateWithContact_UserAlreadyRegistered() throws TelegramApiException {
        String expected = "You already registered";
        Long chatId = 123L;
        Long userId = 1L;
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        List<Update> updates = new ArrayList<>();
        updates.add(update);
        Contact contact = new Contact();
        contact.setPhoneNumber("79538888888");

        ExtendedContactDto currentContact = ExtendedContactDto.builder()
                .userId(userId)
                .tgChatId("123")
                .phone("79538888888")
                .build();

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().hasText()).thenReturn(false);
        when(update.getMessage().hasContact()).thenReturn(true);
        when(message.getChatId()).thenReturn(chatId);

        when(message.getContact()).thenReturn(contact);
        when(userServiceClient.findUserIdByPhoneNumber(anyString())).thenReturn(userId);
        when(userServiceClient.getUserContact(userId)).thenReturn(currentContact);

        doThrow(new TelegramApiException("test ex")).when(notificationTelegramBot).execute(any(SendMessage.class));
        notificationTelegramBot.onUpdateReceived(update);

        verify(notificationTelegramBot).execute(captor.capture());

        SendMessage actual = captor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getText()).isEqualTo(expected);
        assertThat(actual.getChatId()).isEqualTo("123");
    }

    @Test
    void testUpdateWithContact_SuccessfulRegistration() throws TelegramApiException {
        String expected = "Registration is successful! You can get telegram-notifications now. Thank you!";
        Long chatId = 123L;
        Long userId = 1L;
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        List<Update> updates = new ArrayList<>();
        updates.add(update);
        Contact contact = new Contact();
        contact.setPhoneNumber("79538888888");

        ExtendedContactDto currentContact = ExtendedContactDto.builder()
                .userId(userId)
                .tgChatId("anyString")
                .phone("79538888888")
                .build();

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().hasText()).thenReturn(false);
        when(update.getMessage().hasContact()).thenReturn(true);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getContact()).thenReturn(contact);
        when(userServiceClient.findUserIdByPhoneNumber(anyString())).thenReturn(userId);
        when(userServiceClient.getUserContact(userId)).thenReturn(currentContact);

        doThrow(new TelegramApiException("test ex")).when(notificationTelegramBot).execute(any(SendMessage.class));
        notificationTelegramBot.onUpdateReceived(update);

        verify(notificationTelegramBot).execute(captor.capture());

        SendMessage actual = captor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getText()).isEqualTo(expected);
        assertThat(actual.getChatId()).isEqualTo("123");
    }

    @Test
    void testUpdateWithContact_NotCorrectPhone() throws TelegramApiException {
        String expected = "Telegram account is not correct! Please, use you actual account for registration!";
        Long chatId = 123L;
        Long userId = 1L;
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        List<Update> updates = new ArrayList<>();
        updates.add(update);
        Contact contact = new Contact();
        contact.setPhoneNumber("79538888888");

        ExtendedContactDto currentContact = ExtendedContactDto.builder()
                .userId(userId)
                .tgChatId("123")
                .phone("79538888889")
                .build();

        when(update.hasMessage()).thenReturn(true);

        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().hasText()).thenReturn(false);
        when(update.getMessage().hasContact()).thenReturn(true);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getContact()).thenReturn(contact);
        when(userServiceClient.findUserIdByPhoneNumber(anyString())).thenReturn(userId);
        when(userServiceClient.getUserContact(userId)).thenReturn(currentContact);

        doThrow(new TelegramApiException("test ex")).when(notificationTelegramBot).execute(any(SendMessage.class));
        notificationTelegramBot.onUpdateReceived(update);

        verify(notificationTelegramBot).execute(captor.capture());

        SendMessage actual = captor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getText()).isEqualTo(expected);
        assertThat(actual.getChatId()).isEqualTo("123");
    }
}