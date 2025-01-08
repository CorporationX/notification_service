//package faang.school.notificationservice.listener;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import faang.school.notificationservice.client.UserServiceClient;
//import faang.school.notificationservice.dto.UserDto;
//import faang.school.notificationservice.dto.event.GoalCompletedEvent;
//import faang.school.notificationservice.messaging.MessageBuilder;
//import faang.school.notificationservice.service.EmailService;
//import faang.school.notificationservice.service.SmsService;
//import faang.school.notificationservice.service.telegram.NotificationTelegramService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.data.redis.connection.Message;
//
//import static org.mockito.Mockito.*;
//
//class GoalCompletedEventListenerTest {
//
//    @Mock
//    private UserServiceClient userServiceClient;
//
//    @Mock
//    private EmailService emailService;
//
//    @Mock
//    private NotificationTelegramService telegramService;
//
//    @Mock
//    private SmsService smsService;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private MessageBuilder<GoalCompletedEvent> messageBuilder;
//
//    @InjectMocks
//    private GoalCompletedEventListener goalCompletedEventListener;
//
//    private GoalCompletedEvent goalCompletedEvent;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        goalCompletedEvent = new GoalCompletedEvent();
//        goalCompletedEvent.setGoalId(1L);
//        goalCompletedEvent.setUserId(100L);
//    }
//
//    @Test
//    void testOnMessage_Success() {
//        // Мокируем поведение userServiceClient
//        when(userServiceClient.getUserById(anyLong())).thenReturn(new UserDto(100L, "email"));
//
//        // Мокируем получение сообщения
//        Message message = mock(Message.class);
//        when(objectMapper.readValue(message.getBody(), GoalCompletedEvent.class)).thenReturn(goalCompletedEvent);
//
//        // Вызываем onMessage
//        goalCompletedEventListener.onMessage(message, new byte[]{});
//
//        // Проверяем, что сервисы уведомлений были вызваны
//        verify(telegramService, times(1)).sendNotification(anyString());
//        verify(smsService, times(0)).sendSms(anyString(), anyString()); // Если пользователь не предпочел SMS
//        verify(emailService, times(0)).sendEmail(anyString(), anyString()); // Если пользователь не предпочел Email
//    }
//
//    @Test
//    void testOnMessage_EmailNotification() {
//        // Предположим, что пользователь выбрал email
//        when(userServiceClient.getUserById(anyLong())).thenReturn(new User(100L, "email@example.com"));
//
//        // Мокируем получение сообщения
//        Message message = mock(Message.class);
//        when(objectMapper.readValue(message.getBody(), GoalCompletedEvent.class)).thenReturn(goalCompletedEvent);
//
//        // Вызываем onMessage
//        goalCompletedEventListener.onMessage(message, new byte[]{});
//
//        // Проверяем, что email-уведомление было отправлено
//        verify(emailService, times(1)).sendEmail(anyString(), anyString());
//        verify(smsService, times(0)).sendSms(anyString(), anyString()); // SMS не должно быть отправлено
//        verify(telegramService, times(0)).sendNotification(anyString()); // Telegram не должно быть отправлено
//    }
//
//    @Test
//    void testOnMessage_TelegramNotification() {
//        // Предположим, что пользователь выбрал Telegram
//        when(userServiceClient.getUserById(anyLong())).thenReturn(new User(100L, "telegram"));
//
//        // Мокируем получение сообщения
//        Message message = mock(Message.class);
//        when(objectMapper.readValue(message.getBody(), GoalCompletedEvent.class)).thenReturn(goalCompletedEvent);
//
//        // Вызываем onMessage
//        goalCompletedEventListener.onMessage(message, new byte[]{});
//
//        // Проверяем, что Telegram-уведомление было отправлено
//        verify(telegramService, times(1)).sendNotification(anyString());
//        verify(smsService, times(0)).sendSms(anyString(), anyString()); // SMS не должно быть отправлено
//        verify(emailService, times(0)).sendEmail(anyString(), anyString()); // Email не должно быть отправлено
//    }
//}
