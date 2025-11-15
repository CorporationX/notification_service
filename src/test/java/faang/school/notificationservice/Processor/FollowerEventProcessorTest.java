package faang.school.notificationservice.Processor;

import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.processor.FollowerEventProcessor;
import faang.school.notificationservice.service.MessageService;
import faang.school.notificationservice.service.NotificationDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowerEventProcessorTest {

        @Mock
        private MessageService messageService;

        @Mock
        private NotificationDispatcher notificationDispatcher;

        private FollowerEventProcessor processor;

        @BeforeEach
        void setUp() {
            processor = new FollowerEventProcessor(messageService, notificationDispatcher);
        }

        @Test
        void process_ValidEvent_SendsNotification() {
            // Arrange
            FollowerEventDto event = FollowerEventDto.builder()
                    .followerId(1L)
                    .followeeId(2L)
                    .locale(Locale.US)
                    .build();
            String expectedMessage = "john is now following you!";

            when(messageService.buildMessage(eq(FollowerEventDto.class), eq(event), any(Locale.class)))
                    .thenReturn(expectedMessage);

            // Act
            processor.process(event);

            // Assert
            verify(messageService).buildMessage(eq(FollowerEventDto.class), eq(event), any(Locale.class));
            verify(notificationDispatcher).dispatch(2L, expectedMessage);
        }

        @Test
        void process_EventWithoutLocale_UsesDefaultLocale() {
            // Arrange
            FollowerEventDto event = FollowerEventDto.builder()
                    .followerId(1L)
                    .followeeId(2L)
                    .locale(null)
                    .build();

            when(messageService.buildMessage(any(), any(), any(Locale.class)))
                    .thenReturn("message");

            // Act
            processor.process(event);

            // Assert
            verify(messageService).buildMessage(eq(FollowerEventDto.class), eq(event), eq(Locale.getDefault()));
        }

        @Test
        void extractUserId_ReturnsFolloweeId() {
            // Arrange
            FollowerEventDto event = FollowerEventDto.builder()
                    .followerId(1L)
                    .followeeId(2L)
                    .build();

            // Act
            long userId = processor.extractUserId(event);

            // Assert
            assertEquals(2L, userId);
        }

        @Test
        void getEventType_ReturnsCorrectClass() {
            // Act
            Class<FollowerEventDto> eventType = processor.getEventType();

            // Assert
            assertEquals(FollowerEventDto.class, eventType);
        }
}