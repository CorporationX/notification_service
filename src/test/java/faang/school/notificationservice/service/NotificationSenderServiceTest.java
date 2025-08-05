package faang.school.notificationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.enums.PreferredContact;
import faang.school.notificationservice.messaging.like.CommentLikedEventMessageBuilder;
import faang.school.notificationservice.messaging.like.PostLikedEventMessageBuilder;
import faang.school.notificationservice.repository.NotificationRepository;
import faang.school.notificationservice.service.notification.EventType;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.notification.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationSenderServiceTest {

    @Mock
    private List<NotificationService> notificationServices;

    @Mock
    private CommentLikedEventMessageBuilder commentMessageBuilder;

    @Mock
    private PostLikedEventMessageBuilder postMessageBuilder;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationSenderService notificationSenderService;

    private UserDto testUser;
    private AggregatedNotificationsDto aggregatedDto;
    private JsonNode eventData;
    private NotificationService emailService;
    private NotificationService smsService;

    @BeforeEach
    void setUp() {
        emailService = mock(NotificationService.class);
        smsService = mock(NotificationService.class);

        testUser = UserDto.builder()
                .id(1L)
                .email("test@example.com")
                .preference(PreferredContact.EMAIL)
                .locale(Locale.UK)
                .build();

        ObjectMapper realMapper = new ObjectMapper();
        eventData = realMapper.createObjectNode()
                .putObject("owner")
                .put("id", 1L)
                .put("preference", "EMAIL")
                .put("locale", "en_GB");

        aggregatedDto = AggregatedNotificationsDto.builder()
                .receiverId(1L)
                .targetEntityId(100L)
                .eventType(EventType.POST_LIKED)
                .eventData(eventData)
                .build();
    }

    private void setupNotificationServices() {
        when(emailService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);
        when(notificationServices.stream()).thenAnswer(invocation -> Stream.of(emailService, smsService));
    }

    @Test
    void send_ShouldCallCorrectNotificationService_WhenPreferenceMatches() {
        setupNotificationServices();
        String message = "Test Message";
        testUser.setPreference(PreferredContact.EMAIL);

        notificationSenderService.send(testUser, message);

        verify(emailService).send(testUser, message);
        verify(smsService, never()).send(any(), any());
    }

    @Test
    void send_ShouldThrowException_WhenNoServiceMatchesPreference() {
        setupNotificationServices();
        String message = "Test Message";
        testUser.setPreference(PreferredContact.TELEGRAM);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> notificationSenderService.send(testUser, message));

        assertEquals("No notification service was found for user preferred notification type", exception.getMessage());
    }

    @Test
    void sendAggregatedNotifications_ShouldSendPostLikedNotificationSuccessfully() {
        setupNotificationServices();
        String message = "Your post was liked!";
        aggregatedDto.setEventType(EventType.POST_LIKED);

        when(objectMapper.convertValue(eventData.get("owner"), UserDto.class)).thenReturn(testUser);
        when(postMessageBuilder.buildMessage(aggregatedDto, Locale.UK)).thenReturn(message);

        notificationSenderService.sendAggregatedNotifications(aggregatedDto);

        verify(objectMapper).convertValue(eventData.get("owner"), UserDto.class);
        verify(postMessageBuilder).buildMessage(aggregatedDto, Locale.UK);
        verify(emailService).send(testUser, message);
        verify(notificationRepository).updateStatusByGroup(
                1L, 100L, EventType.POST_LIKED.name(), NotificationStatus.SENT.name());
        verify(notificationRepository, never()).updateStatusByGroup(anyLong(), anyLong(), anyString(), eq(NotificationStatus.FAILED.name()));
    }

    @Test
    void sendAggregatedNotifications_ShouldSendCommentLikedNotificationSuccessfully() {
        setupNotificationServices();
        String message = "Your comment was liked!";
        aggregatedDto.setEventType(EventType.COMMENT_LIKED);

        when(objectMapper.convertValue(eventData.get("owner"), UserDto.class)).thenReturn(testUser);
        when(commentMessageBuilder.buildMessage(aggregatedDto, Locale.UK)).thenReturn(message);

        notificationSenderService.sendAggregatedNotifications(aggregatedDto);

        verify(objectMapper).convertValue(eventData.get("owner"), UserDto.class);
        verify(commentMessageBuilder).buildMessage(aggregatedDto, Locale.UK);
        verify(emailService).send(testUser, message);
        verify(notificationRepository).updateStatusByGroup(
                1L, 100L, EventType.COMMENT_LIKED.name(), NotificationStatus.SENT.name());
        verify(notificationRepository, never()).updateStatusByGroup(anyLong(), anyLong(), anyString(), eq(NotificationStatus.FAILED.name()));
    }

    @Test
    void sendAggregatedNotifications_ShouldUpdateStatusToFailed_WhenSendingThrowsException() {
        aggregatedDto.setEventType(EventType.POST_LIKED);
        RuntimeException testException = new RuntimeException("Message builder failed");

        when(objectMapper.convertValue(eventData.get("owner"), UserDto.class)).thenReturn(testUser);
        when(postMessageBuilder.buildMessage(aggregatedDto, Locale.UK)).thenThrow(testException);

        notificationSenderService.sendAggregatedNotifications(aggregatedDto);

        verify(notificationRepository).updateStatusByGroup(
                1L, 100L, EventType.POST_LIKED.name(), NotificationStatus.FAILED.name());
        verify(notificationRepository, never()).updateStatusByGroup(anyLong(), anyLong(), anyString(), eq(NotificationStatus.SENT.name()));
        verify(emailService, never()).send(any(), any());
    }

    @Test
    void sendAggregatedNotifications_ShouldUseDefaultLocale_WhenUserLocaleIsNull() {
        setupNotificationServices();
        String message = "Your post was liked!";
        aggregatedDto.setEventType(EventType.POST_LIKED);
        testUser.setLocale(null);
        Locale defaultLocale = Locale.getDefault();

        when(objectMapper.convertValue(eventData.get("owner"), UserDto.class)).thenReturn(testUser);
        when(postMessageBuilder.buildMessage(aggregatedDto, defaultLocale)).thenReturn(message);

        notificationSenderService.sendAggregatedNotifications(aggregatedDto);

        verify(postMessageBuilder).buildMessage(aggregatedDto, defaultLocale);
        verify(emailService).send(testUser, message);
    }
}