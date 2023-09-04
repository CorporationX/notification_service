package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.JsonMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {
    @Mock
    JsonMapper jsonMapper;
    @Mock
    UserServiceClient userServiceClient;
    @Mock
    MessageBuilder<Object> messageBuilder;
    List<MessageBuilder<Object>> messageBuilders = new ArrayList<>();
    @Mock
    NotificationService notificationService;
    List<NotificationService> notificationServices = new ArrayList<>();

    EventListenerBase<Object> abstractEventListener;

    private UserDto userDto;
    Object event = new Object();

    @BeforeEach
    void setUp() {
        messageBuilders = new ArrayList<>(Arrays.asList(messageBuilder));
        notificationServices = new ArrayList<>(Arrays.asList(notificationService));
        abstractEventListener = new EventListenerBase<>(jsonMapper, userServiceClient, messageBuilders, notificationServices);
        userDto = UserDto.builder()
                .id(1)
                .email("test@yandex.ru")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }

    @Test
    void getMessageTest() {
        abstractEventListener.getMessage(event, Locale.UK);
        Mockito.verify(messageBuilder).buildMessage(event, Locale.UK);
    }

    @Test
    void sendNotificationTest() {
        Mockito.when(userServiceClient.getUserDtoForNotification(Mockito.anyLong())).thenReturn(userDto);
        Mockito.when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        String message = "message";

        abstractEventListener.sendNotification(1, message);
        Mockito.verify(notificationService, Mockito.times(1)).send(userDto, message);
    }

    @Test
    void sendNotificationNoServiceTest() {
        Mockito.when(userServiceClient.getUserDtoForNotification(Mockito.anyLong())).thenReturn(userDto);
        Mockito.when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);
        String message = "message";

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> abstractEventListener.sendNotification(1, message)
        );
        Mockito.verify(notificationService, Mockito.times(0)).send(userDto, message);
    }

}
