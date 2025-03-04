package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.dto.event.TestEvent;
import faang.school.notificationservice.dto.event.UnusedTestEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.TestMessageBuilder;
import faang.school.notificationservice.messaging.UnusedTestMessageBuilder;
import faang.school.notificationservice.service.MailService;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private UserServiceClient userServiceClient;

    private NotificationService notificationService;
    private MessageBuilder<TestEvent> messageBuilder;
    private NotificationService unusedNotificationService;
    private MessageBuilder<UnusedTestEvent> unusedMessageBuilder;
    private AbstractEventListener<TestEvent> eventListener;

    @BeforeEach
    void setUp() {
        notificationService = Mockito.mock(MailService.class);
        when(notificationService.getPreferredContact()).thenReturn(UserNotificationDto.PreferredContact.EMAIL);

        unusedNotificationService = Mockito.mock(SmsService.class);
        when(unusedNotificationService.getPreferredContact()).thenReturn(UserNotificationDto.PreferredContact.SMS);

        messageBuilder = Mockito.spy(TestMessageBuilder.class);
        unusedMessageBuilder = Mockito.spy(UnusedTestMessageBuilder.class);

        Set<NotificationService> notificationServices = Set.of(notificationService, unusedNotificationService);
        HashMap<UserNotificationDto.PreferredContact, NotificationService> notificationServicesMap = new HashMap<>();
        for (NotificationService notificationService : notificationServices) {
            notificationServicesMap.put(notificationService.getPreferredContact(), notificationService);
        }

        Set<MessageBuilder<?>> messageBuilders = Set.of(messageBuilder, unusedMessageBuilder);
        Map<Class<?>, MessageBuilder<?>> messageBuildersMap = new HashMap<>();
        for (MessageBuilder<?> messageBuilder : messageBuilders) {
            messageBuildersMap.put(messageBuilder.getInstance(), messageBuilder);
        }

        eventListener = new AbstractEventListener<>(userServiceClient,
                notificationServicesMap,
                messageBuildersMap);
    }

    @Test
    void testGetMessage() {
        TestEvent event = new TestEvent();
        Locale locale = Locale.ENGLISH;
        String expectedMessage = "Test message";

        when(messageBuilder.buildMessage(event, locale)).thenReturn(expectedMessage);

        String actualMessage = eventListener.getMessage(event, locale);

        assertEquals(expectedMessage, actualMessage);
        verify(messageBuilder).buildMessage(event, locale);
        verify(unusedMessageBuilder, never()).buildMessage(any(), any());
    }

    @Test
    void testSendNotification() {
        long userId = 1L;
        String message = "Test message";
        UserNotificationDto userNotificationDto = new UserNotificationDto();
        userNotificationDto.setPreference(UserNotificationDto.PreferredContact.EMAIL);

        when(userServiceClient.getUserNotificationDto(userId)).thenReturn(userNotificationDto);

        eventListener.sendNotification(userId, message);

        verify(userServiceClient, times(1)).getUserNotificationDto(userId);
        verify(notificationService, times(1)).send(userNotificationDto, message);
        verify(unusedNotificationService, never()).send(any(), any());
    }
}