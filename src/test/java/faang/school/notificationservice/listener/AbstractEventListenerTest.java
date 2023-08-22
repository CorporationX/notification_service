package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.EventStartMessageBuilder;
import faang.school.notificationservice.messageBuilder.MessageBuilder;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private EventStartMessageBuilder eventStartMessageBuilder;
    @Mock
    private EmailService emailService;
    private List<NotificationService> notificationServices;
    private List<MessageBuilder<EventDto, String>> messageBuilders;
    private DefaultEventListener<EventDto, String> abstractEventListener;
    EventDto event = new EventDto();
    UserDto user = new UserDto();

    @BeforeEach
    void setUp() {
        event.setDescription("SomeDescription");
        event.setId(0L);
        event.setTitle("SomeTitle");
        event.setStartDate(LocalDateTime.now().plusMinutes(11));
        user.setId(1L);
        user.setEmail("some@email.com");
        user.setPreference(UserDto.PreferredContact.EMAIL);
        event.setUserDto(user);
        notificationServices = new ArrayList<>();
        notificationServices.add(emailService);
        messageBuilders = new ArrayList<>();
        messageBuilders.add(eventStartMessageBuilder);
        abstractEventListener = new DefaultEventListener<>(userServiceClient, notificationServices, messageBuilders);
    }

    @Test
    void getMessage_Test() {
        when(eventStartMessageBuilder.getEventType()).thenReturn(EventDto.class);
        when(eventStartMessageBuilder.buildMessage(event, "someString")).thenReturn("SomeMessage");

        String result = abstractEventListener.getMessage(EventDto.class, "someString", event);

        assertEquals("SomeMessage", result);
    }

    @Test
    void sendNotificationTest() {
        when(userServiceClient.getUser(1L)).thenReturn(user);
        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

		abstractEventListener.sendNotification(1L, "SomeMessage");

        verify(emailService).send(user, "SomeMessage");
    }
}