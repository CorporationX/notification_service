package faang.school.notificationservice.listener.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventStartDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventStartListenerTest {
    @InjectMocks
    private EventStartListener eventStartListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private ScheduledThreadPoolExecutor scheduledExecutor;
    private EventStartDto event;
    private Message message;

    @BeforeEach
    void setUp() throws IOException {
        event = mock(EventStartDto.class);
        when(event.getStartDate()).thenReturn(LocalDateTime.of(2023, 1, 1, 0, 0));

        message = mock(Message.class);
        byte[] body = new byte[0];

        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, EventStartDto.class)).thenReturn(event);
    }

    @Test
    void onMessage_shouldInvokeReadValueMethod() throws IOException {
        eventStartListener.onMessage(message, null);
        verify(objectMapper).readValue(message.getBody(), EventStartDto.class);
    }

    @Test
    void onMessage_shouldInvokeGetUsersByIdsMethod() {
        eventStartListener.onMessage(message, null);
        verify(userServiceClient).getUsersByIds(event.getAttendeeIds());
    }

    @Test
    void onMessage_shouldInvokeExecutorScheduleMethodFiveTimes() {
        eventStartListener.onMessage(message, null);
        verify(scheduledExecutor, times(5))
                .schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }

    @Test
    void onMessage_shouldInvokeEventGetStartDateFiveTimes() {
        eventStartListener.onMessage(message, null);
        verify(event, times(5)).getStartDate();
    }

    @Test
    void onMessageShouldInvokeEventSetTimeTillStartFiveTimes() {
        eventStartListener.onMessage(message, null);
        verify(event, times(5)).setTimeTillStart(anyLong());
    }
}