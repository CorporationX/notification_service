package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.data.NotificationChannel;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.AchievementEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserFeignService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementEventListenerTest {
    @Mock
    private UserFeignService userFeignService;

    @Mock
    private MessageBuilder<AchievementEvent> messageBuilder;

    @Mock
    private List<NotificationService> notificationServices;

    @InjectMocks
    private AchievementEventListener achievementEventListener;

    private AchievementEvent event;
    private UserContactsDto userContactsDto;

    @BeforeEach
    void setUp() {
        event = new AchievementEvent("User name", 1L, "Achievement title", 1L);
        userContactsDto = UserContactsDto.builder()
                .id(1L)
                .username("john_doe")
                .email("user@example.com")
                .phone("+1234567890")
                .preference(NotificationChannel.EMAIL)
                .menteesId(List.of(2L, 3L))
                .mentorsId(List.of(4L))
                .skillsId(List.of(5L, 6L))
                .build();
    }

    @Test
    @DisplayName("Test handleEvent method success")
    void testHandleEventSuccess() {
        when(userFeignService.getUserContacts(event.getUserId())).thenReturn(userContactsDto);

        when(messageBuilder.buildMessage(event, LocaleContextHolder.getLocale())).thenReturn("Achievement unlocked!");

        NotificationService mockNotificationService = mock(NotificationService.class);
        when(notificationServices.stream()).thenReturn(Stream.of(mockNotificationService));
        when(mockNotificationService.getPreferredContact()).thenReturn(NotificationChannel.EMAIL);
        doNothing().when(mockNotificationService).send(eq(userContactsDto), eq("Achievement unlocked!"));

        achievementEventListener.handleEvent(event);

        verify(userFeignService, times(1)).getUserContacts(event.getUserId());
        verify(mockNotificationService, times(1)).send(eq(userContactsDto), eq("Achievement unlocked!"));
    }

    @Test
    @DisplayName("Test handleEvent method with UserService failure")
    void testHandleEventUserServiceFailure() {
        when(userFeignService.getUserContacts(event.getUserId())).thenThrow(FeignException.class);

        FeignException exception = assertThrows(FeignException.class, () -> {
            achievementEventListener.handleEvent(event);
        });

        assertNotNull(exception);
        verify(userFeignService, times(1)).getUserContacts(event.getUserId());

        verify(notificationServices, never()).stream();
    }
}
