package faang.school.notificationservice.listener.event;

import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.mapper.JsonMapper;
import faang.school.notificationservice.service.EventStartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventStartListenerTest {
    @InjectMocks
    private EventStartListener eventStartListener;
    @Mock
    private JsonMapper<EventStartDto> jsonMapper;
    @Mock
    private EventStartService eventStartService;
    @Mock
    private EventStartDto eventStartDto;
    private Message message;

    @BeforeEach
    void setUp() {
        message = mock(Message.class);
        byte[] body = new byte[0];

        when(message.getBody()).thenReturn(body);
        when(jsonMapper.toObject(message.getBody(), EventStartDto.class)).thenReturn(eventStartDto);
    }

    @Test
    void onMessage() {
        eventStartListener.onMessage(message, new byte[0]);
        verify(eventStartService).scheduleNotifications(any());
    }
}