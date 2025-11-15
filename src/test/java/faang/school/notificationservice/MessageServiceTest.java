package faang.school.notificationservice;

import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import faang.school.notificationservice.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageBuilder<FollowerEventDto> followerMessageBuilder;

    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageService(List.of(followerMessageBuilder));
    }

    @Test
    void buildMessage_BuilderExists_ReturnsMessage() {
        // Arrange
        FollowerEventDto event = FollowerEventDto.builder()
                .followerId(1L)
                .followeeId(2L)
                .build();
        Locale locale = Locale.ENGLISH;
        String expectedMessage = "New follower: 1";

        when(followerMessageBuilder.getInstance()).thenReturn((Class) FollowerEventDto.class);
        when(followerMessageBuilder.buildMessage(event, locale)).thenReturn(expectedMessage);

        // Act
        String result = messageService.buildMessage(FollowerEventDto.class, event, locale);

        // Assert
        assertEquals(expectedMessage, result);
        verify(followerMessageBuilder).buildMessage(event, locale);
        verify(followerMessageBuilder).getInstance();
    }

    @Test
    void buildMessage_BuilderNotFound_ThrowsException() {
        // Arrange
        FollowerEventDto event = FollowerEventDto.builder().build();
        when(followerMessageBuilder.getInstance()).thenReturn((Class) String.class);

        // Act & Assert
        MessageBuilderNotFoundException exception = assertThrows(
                MessageBuilderNotFoundException.class,
                () -> messageService.buildMessage(FollowerEventDto.class, event, Locale.ENGLISH)
        );

        assertTrue(exception.getMessage().contains("FollowerEventDto"));
    }

    @Test
    void buildMessage_DifferentLocales_CallsBuilderWithCorrectLocale() {
        // Arrange
        FollowerEventDto event = FollowerEventDto.builder().followerId(1L).build();
        Locale russianLocale = new Locale("ru");

        when(followerMessageBuilder.getInstance()).thenReturn((Class) FollowerEventDto.class);
        when(followerMessageBuilder.buildMessage(eq(event), any(Locale.class)))
                .thenReturn("сообщение");

        // Act
        messageService.buildMessage(FollowerEventDto.class, event, russianLocale);

        // Assert
        verify(followerMessageBuilder).buildMessage(event, russianLocale);
    }

    @Test
    void buildMessage_MultipleBuilders_SelectsCorrectOne() {
        // Arrange
        MessageBuilder<String> stringBuilder = mock(MessageBuilder.class);
        MessageService service = new MessageService(List.of(followerMessageBuilder, stringBuilder));

        FollowerEventDto event = FollowerEventDto.builder().followerId(1L).build();

        when(followerMessageBuilder.getInstance()).thenReturn((Class) FollowerEventDto.class);
        when(followerMessageBuilder.buildMessage(any(), any())).thenReturn("message");

        // Act
        service.buildMessage(FollowerEventDto.class, event, Locale.ENGLISH);

        // Assert
        verify(followerMessageBuilder).buildMessage(any(), any());
        verify(stringBuilder, never()).buildMessage(any(), any());
    }
}