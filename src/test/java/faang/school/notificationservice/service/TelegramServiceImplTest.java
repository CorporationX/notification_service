package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ExtendedContactDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.telegram.NotificationTelegramBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramServiceImplTest {
    @Mock
    NotificationTelegramBot notificationTelegramBot;
    @Mock
    UserServiceClient userServiceClient;
    @InjectMocks
    TelegramServiceImpl telegramService;

    @Test
    void testSend_UserIsNotVerify() {
        UserDto user = new UserDto();
        user.setId(1L);
        ExtendedContactDto extendedContactDto = ExtendedContactDto
                .builder()
                .userId(1L)
                .tgChatId(null)
                .build();

        when(userServiceClient.getUserContact(1L)).thenReturn(extendedContactDto);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> telegramService.send(user, anyString()));

        assertEquals("User's chatId is not verify in TG-notification system", exception.getMessage());
    }

    @Test
    void testSend_UserVerify() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setId(1L);
        ExtendedContactDto extendedContactDto = ExtendedContactDto
                .builder()
                .userId(1L)
                .tgChatId("anyChatId")
                .build();

        when(userServiceClient.getUserContact(1L)).thenReturn(extendedContactDto);
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        telegramService.send(user, "message");
        verify(notificationTelegramBot).execute(captor.capture());
        assertThat(captor.getValue().getChatId()).isEqualTo(extendedContactDto.getTgChatId());
        assertThat(captor.getValue().getText()).isEqualTo("message");
    }
}