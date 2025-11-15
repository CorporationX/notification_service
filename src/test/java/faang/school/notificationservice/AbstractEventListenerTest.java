package faang.school.notificationservice;

import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.messaging.listeners.AbstractEventListener;
import faang.school.notificationservice.processor.EventProcessor;
import faang.school.notificationservice.service.EventDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private EventDeserializer eventDeserializer;

    @Mock
    private EventProcessor<FollowerEventDto> eventProcessor;

    @Mock
    private Acknowledgment acknowledgment;

    private TestEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new TestEventListener(eventDeserializer, eventProcessor);
    }

    @Test
    void handleEvent_ValidEvent_AcknowledgesMessage() {
        // Arrange
        String eventJson = "{\"follower_id\":1}";
        FollowerEventDto event = FollowerEventDto.builder().followerId(1L).build();

        when(eventProcessor.getEventType()).thenReturn(FollowerEventDto.class);
        when(eventDeserializer.deserialize(eventJson, FollowerEventDto.class))
                .thenReturn(event);

        // Act
        listener.handleEvent(eventJson, acknowledgment);

        // Assert
        verify(eventDeserializer).deserialize(eventJson, FollowerEventDto.class);
        verify(eventProcessor).process(event);
        verify(acknowledgment).acknowledge();
    }

    @Test
    void handleEvent_DeserializationFails_DoesNotAcknowledge() {
        // Arrange
        String eventJson = "invalid";

        when(eventProcessor.getEventType()).thenReturn(FollowerEventDto.class);
        when(eventDeserializer.deserialize(any(), any()))
                .thenThrow(new RuntimeException("Deserialization failed"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> listener.handleEvent(eventJson, acknowledgment));

        verify(eventProcessor, never()).process(any());
        verify(acknowledgment, never()).acknowledge();
    }

    @Test
    void handleEvent_ProcessingFails_DoesNotAcknowledge() {
        // Arrange
        String eventJson = "{\"follower_id\":1}";
        FollowerEventDto event = FollowerEventDto.builder().build();

        when(eventProcessor.getEventType()).thenReturn(FollowerEventDto.class);
        when(eventDeserializer.deserialize(eventJson, FollowerEventDto.class))
                .thenReturn(event);
        doThrow(new RuntimeException("Processing failed"))
                .when(eventProcessor).process(event);

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> listener.handleEvent(eventJson, acknowledgment));

        verify(acknowledgment, never()).acknowledge();
    }

    // Test helper class
    private static class TestEventListener extends AbstractEventListener<FollowerEventDto> {
        public TestEventListener(EventDeserializer deserializer,
                                 EventProcessor<FollowerEventDto> processor) {
            super(deserializer, processor);
        }
    }
}