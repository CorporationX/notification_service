package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.LikePostEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.LikePostMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikePostEventListenerTest {

    @InjectMocks
    private LikePostEventListener likePostEventListener;

    @Mock
    private Message message;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private LikePostMessageBuilder likePostMessageBuilder;

    @Mock
    private EmailService emailService;

    private Map<Class<?>, MessageBuilder<?>> messageBuilders;
    private Map<UserDto.PreferredContact, NotificationService> notificationServices;

    private static final long ID = 1L;
    private static final String MESSAGE = "Success";
    private LikePostEvent likePostEvent;
    private UserDto userDto;


    @BeforeEach
    public void init() {
        messageBuilders = new HashMap<>();
        notificationServices = new HashMap<>();
        likePostEventListener = new LikePostEventListener(
                objectMapper, userServiceClient,
                messageBuilders, notificationServices);

        likePostEvent = LikePostEvent.builder()
                .postAuthorId(ID)
                .likeAuthorId(ID)
                .postId(ID)
                .build();
        userDto = UserDto.builder()
                .notifyPreference(UserDto.PreferredContact.EMAIL)
                .build();
    }

    @Nested
    class PositiveTest {
        @Test
        @DisplayName("Успешная отправка уведомления пользователю")
        public void whenOnMessageWithCurrentDataThenSuccess() throws IOException {
            when(objectMapper.readValue(message.getBody(), LikePostEvent.class)).thenReturn(likePostEvent);
            when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
            messageBuilders.put(LikePostEvent.class, likePostMessageBuilder);
            notificationServices.put(UserDto.PreferredContact.EMAIL, emailService);
            when(likePostMessageBuilder.buildMessage(eq(likePostEvent), any(Locale.class))).thenReturn(MESSAGE);
            doNothing().when(emailService).send(userDto, MESSAGE);

            likePostEventListener.onMessage(message, new byte[0]);

            verify(objectMapper).readValue(message.getBody(), LikePostEvent.class);
            verify(userServiceClient).getUser(anyLong());
            verify(likePostMessageBuilder).buildMessage(eq(likePostEvent), any(Locale.class));
            verify(emailService).send(userDto, MESSAGE);
        }
    }

    @Nested
    class NegativeTests {

        @Test
        @DisplayName("Ошибка при отсутствии соответствующего messageBuilder")
        public void whenOnMessageWithoutMessageBuilderThenException() throws IOException {
            when(objectMapper.readValue(message.getBody(), LikePostEvent.class)).thenReturn(likePostEvent);

            assertThrows(NoSuchElementException.class, () -> likePostEventListener.onMessage(message, new byte[0]));
        }

        @Test
        @DisplayName("Ошибка при отсутствии соответствующего notificationService")
        public void whenOnMessageWithoutNotificationServiceThenException() throws IOException {
            when(objectMapper.readValue(message.getBody(), LikePostEvent.class)).thenReturn(likePostEvent);
            when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
            messageBuilders.put(LikePostEvent.class, likePostMessageBuilder);
            when(likePostMessageBuilder.buildMessage(eq(likePostEvent), any(Locale.class))).thenReturn(MESSAGE);

            assertThrows(NoSuchElementException.class, () -> likePostEventListener.onMessage(message, new byte[0]));
        }
    }
}