package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.event.EventStartDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EventStartServiceTest {
    @InjectMocks
    private EventStartService eventStartService;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private ScheduledThreadPoolExecutor scheduledExecutor;
    private EventStartDto eventStartDto;

    @BeforeEach
    void setUp() {
        eventStartDto = EventStartDto.builder()
                .id(1L)
                .attendeeIds(List.of(1L))
                .build();

        EventDto eventDto = EventDto.builder()
                .id(1L)
                .startDate(LocalDateTime.now())
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .build();
        List<UserDto> userDtoList = List.of(userDto);

        when(userServiceClient.getEvent(1L)).thenReturn(eventDto);
        when(userServiceClient.getUsersByIds(List.of(1L))).thenReturn(userDtoList);
    }

    @Test
    void clean_shouldInvokeShutdownMethod() {
        eventStartService.clean();
        verify(scheduledExecutor).shutdown();
    }

    @Test
    void scheduleNotifications_shouldInvokeScheduleMethodFiveTimes() {
        eventStartService.scheduleNotifications(eventStartDto);
        verify(scheduledExecutor, times(5))
                .schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }
}