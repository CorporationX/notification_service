package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.like.LikeEventDto;
import faang.school.notificationservice.notification.LikeMessageBuilder;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {
    @Mock
    JsonMapper jsonMapper;
    @Mock
    UserServiceClient userServiceClient;
    @Mock
    LikeMessageBuilder likeMessageBuilder;
    @Mock
    UserServiceClient userClient;
    @Mock
    MessageBuilder<Object> messageBuilder; ;
    @Mock
    NotificationService notificationService;

    UserDto userDto;
    Object event = new Object();
    List<NotificationService> notificationServices = new ArrayList<>();
    List<MessageBuilder<Object>> messageBuilders = new ArrayList<>();
    EventListenerBase<Object> eventListenerBase;
    @BeforeEach
    void setUp() {
        messageBuilders = new ArrayList<>(Arrays.asList(messageBuilder));
        notificationServices = new ArrayList<>(Arrays.asList(notificationService));
        eventListenerBase = new EventListenerBase<>(jsonMapper, userServiceClient, messageBuilders, notificationServices);
        userDto = UserDto.builder()
                .id(1)
                .preference(UserDto.PreferredContact.PHONE)
                .build();
    }

    @Test
    void successful() {
        eventListenerBase.getMessage(event, Locale.ENGLISH);
        Mockito.verify(messageBuilder).buildMessage(event, Locale.ENGLISH);
    }

    @Test
    void sendNotificationTest() {
        Mockito.when(userServiceClient.getUserDtoForNotification(Mockito.anyLong()))
                .thenReturn(userDto);
        Mockito.when(notificationService.getPreferredContact())
                .thenReturn(UserDto.PreferredContact.PHONE);
        String message = "message";

        eventListenerBase.sendNotification(1, message);
        Mockito.verify(notificationService, Mockito.times(1)).send(userDto, message);
    }
}