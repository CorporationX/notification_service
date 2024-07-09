package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataNotificationException;
import faang.school.notificationservice.model.TelegramChat;
import faang.school.notificationservice.repository.TelegramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @InjectMocks
    private TelegramService telegramService;

    @Mock
    private TelegramRepository telegramRepository;

    @Mock
    private TelegramBot telegramBot;

    private UserDto user;
    private final Long chatId = 1L;
    private final String message = "Message";
    private final String messagesHeader = "MessagesHeader";

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setId(1L);
        user.setUsername("User");
    }

    @Test
    void sendNotificationTest() {
        when(telegramRepository.findByPostAuthorId(user.getId())).thenReturn(Optional.of(new TelegramChat(1L, chatId, 1L)));

        telegramService.send(user, message, messagesHeader);

        verify(telegramBot).sendNotification(chatId, message, messagesHeader);
    }

    @Test
    void sendShouldThrowExceptionWhenChatIdNotFound() {
        when(telegramRepository.findByPostAuthorId(anyLong())).thenReturn(Optional.empty());

        DataNotificationException dataValidationException = assertThrows(DataNotificationException.class, () -> telegramService.send(user, message, messagesHeader));

        assertEquals("Пользователь с id: " + user.getId() + " не авторизовался в CorporationX", dataValidationException.getMessage());
    }
}
