package faang.school.notificationservice.handler;

import faang.school.notificationservice.entity.Event;
import faang.school.notificationservice.exception.impl.non_retryable.DuplicateEventException;
import faang.school.notificationservice.service.EventService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventHandlerTest {
    private static final UUID EVENT_ID = UUID.randomUUID();
    public static final LocalDateTime CURRENT_TIME = LocalDateTime.now();
    private final Event testEvent = new Event();

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventHandler eventHandler;

    private ConsumerRecord<String, Object> kafkaEvent;

    @BeforeEach
    void setUp() {
        testEvent.setId(EVENT_ID);
        testEvent.setProcessedAt(CURRENT_TIME);
        kafkaEvent = mock(ConsumerRecord.class);
        when(kafkaEvent.key()).thenReturn(EVENT_ID.toString());
    }

    @Test
    void checkEventDuplicatedThrow() {
        when(eventService.existsById(EVENT_ID)).thenReturn(true);

        assertThrows(DuplicateEventException.class, () -> eventHandler.checkEventDuplicatedThrow(kafkaEvent));
    }

    @Test
    void checkEventDuplicatedNotThrow() {
        when(eventService.existsById(EVENT_ID)).thenReturn(false);

        assertDoesNotThrow(() -> eventHandler.checkEventDuplicatedThrow(kafkaEvent));
    }

    @Test
    void saveEventSuccess() {
        when(eventService.existsById(EVENT_ID)).thenReturn(false);
        eventHandler.checkEventDuplicatedThrow(kafkaEvent);
        assertDoesNotThrow(() -> eventHandler.saveEvent());
    }
}