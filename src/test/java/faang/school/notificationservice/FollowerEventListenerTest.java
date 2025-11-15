package faang.school.notificationservice;


import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.messaging.listeners.FollowerEventListener;
import faang.school.notificationservice.processor.EventProcessor;
import faang.school.notificationservice.service.EventDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Mock
    private EventDeserializer eventDeserializer;

    @Mock
    private EventProcessor<FollowerEventDto> eventProcessor;

    @Mock
    private Acknowledgment acknowledgment;

    private FollowerEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new FollowerEventListener(eventDeserializer, eventProcessor);
    }

    @Test
    void listen_ValidEvent_CallsHandleEvent() {
        // Arrange
        String eventJson = "{\"followerId\":1,\"followeeId\":2}";
        FollowerEventDto event = FollowerEventDto.builder()
                .followerId(1L)
                .followeeId(2L)
                .build();

        when(eventProcessor.getEventType()).thenReturn(FollowerEventDto.class);
        when(eventDeserializer.deserialize(eventJson, FollowerEventDto.class))
                .thenReturn(event);

        // Act
        listener.listen(eventJson, acknowledgment);

        // Assert
        verify(eventDeserializer).deserialize(eventJson, FollowerEventDto.class);
        verify(eventProcessor).process(event);
        verify(acknowledgment).acknowledge();
    }

    @Test
    void listen_InvalidEvent_ThrowsException() {
        // Arrange
        String invalidJson = "invalid";

        when(eventProcessor.getEventType()).thenReturn(FollowerEventDto.class);
        when(eventDeserializer.deserialize(any(), any()))
                .thenThrow(new RuntimeException("Invalid JSON"));

        // Act & Assert
        try {
            listener.listen(invalidJson, acknowledgment);
        } catch (RuntimeException e) {
            // Expected
        }

        verify(acknowledgment, never()).acknowledge();
    }

    @Test
    void listen_ProcessorFails_DoesNotAcknowledge() {
        // Arrange
        String eventJson = "{\"followerId\":1}";
        FollowerEventDto event = FollowerEventDto.builder().build();

        when(eventProcessor.getEventType()).thenReturn(FollowerEventDto.class);
        when(eventDeserializer.deserialize(any(), any())).thenReturn(event);
        doThrow(new RuntimeException("Processing error"))
                .when(eventProcessor).process(event);

        // Act & Assert
        try {
            listener.listen(eventJson, acknowledgment);
        } catch (RuntimeException e) {
            // Expected
        }

        verify(acknowledgment, never()).acknowledge();
    }
}