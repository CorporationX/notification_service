package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.messageBuilder.EventStartEventMessageBuilder;
import faang.school.notificationservice.service.EmailNotificationService;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.SmsNotificationService;
import faang.school.notificationservice.service.TelegramNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventStartEventListenerTest {

    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private JsonObjectMapper jsonObjectMapper;
    @Mock
    private EventStartEventMessageBuilder messageBuilder;
    @Mock
    private EmailNotificationService emailNotificationService;
    @Mock
    private SmsNotificationService smsNotificationService;
    @Mock
    private TelegramNotificationService telegramNotificationService;
    @Mock
    private Message message;

    private List<NotificationService> notificationServices;

    private EventStartEventListener eventStartEventListener;


    @BeforeEach
    void setUp() {
        emailNotificationService = new EmailNotificationService();
        smsNotificationService = new SmsNotificationService();
        telegramNotificationService = new TelegramNotificationService();
        notificationServices = new ArrayList<>();
        notificationServices.add(emailNotificationService);
        notificationServices.add(smsNotificationService);
        notificationServices.add(telegramNotificationService);
        eventStartEventListener = new EventStartEventListener(userServiceClient, jsonObjectMapper, messageBuilder, notificationServices);
    }

    @Test
    void onMessageTest() {
        UserDto first = UserDto.builder()
                .id(1L)
                .username("Kiril")
                .email("kiril.sonyashnikov.77@gmail.com")
                .phone("+74232497777")
                .locale(Locale.GERMANY)
                .preferredContact(UserDto.PreferredContact.TELEGRAM)
                .build();
        UserDto second = UserDto.builder()
                .id(2L)
                .username("David")
                .email("David.kolesnikov.93@gmail.com")
                .phone("+748995412")
                .locale(Locale.CANADA)
                .preferredContact(UserDto.PreferredContact.EMAIL)
                .build();
        EventStartEventDto eventStartEventDto = EventStartEventDto.builder()
                .id(1)
                .title("Halloween")
                .userIds(List.of(1L, 2L))
                .build();

        String json = "{\"id\": 1, \"title\": \"Halloween\", \"userIds\": [1, 2]}";

        byte[] jsonBytes = json.getBytes();

        String test = "halloween";

        when(message.getBody()).thenReturn(jsonBytes);
        when(jsonObjectMapper.readValue(jsonBytes, EventStartEventDto.class)).thenReturn(eventStartEventDto);
        when(userServiceClient.getUser(Mockito.anyLong()))
                .thenReturn(first)
                .thenReturn(second);
        when(messageBuilder.buildMessage(any(Locale.class), Mockito.anyString()))
                .thenReturn("Hooray, our event \"Halloween\" has just started, join us soon!");

        eventStartEventListener.onMessage(message, new byte[0]);

        verify(jsonObjectMapper).readValue(message.getBody(), EventStartEventDto.class);
        verify(userServiceClient, Mockito.times(2)).getUser(Mockito.anyLong());
        verify(messageBuilder, Mockito.times(2)).buildMessage(any(Locale.class), Mockito.anyString());
    }
}