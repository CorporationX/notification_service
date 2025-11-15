package faang.school.notificationservice.builder;

import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.messaging.message_builder.FollowerMessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class FollowerMessageBuilderTest {

    private FollowerMessageBuilder messageBuilder;

    @BeforeEach
    void setUp() {
        messageBuilder = new FollowerMessageBuilder();
    }

    @Test
    void buildMessage_ValidEvent_ReturnsFormattedMessage() {
        // Arrange
        FollowerEventDto event = FollowerEventDto.builder()
                .followerId(123L)
                .followeeId(456L)
                .followTime(LocalDateTime.now())
                .build();

        // Act
        String result = messageBuilder.buildMessage(event, Locale.ENGLISH);

        // Assert
        assertNotNull(result);
        assertEquals("New follower: 123", result);
    }

    @Test
    void buildMessage_DifferentFollowerId_ReturnsCorrectId() {
        // Arrange
        FollowerEventDto event = FollowerEventDto.builder()
                .followerId(999L)
                .followeeId(1L)
                .build();

        // Act
        String result = messageBuilder.buildMessage(event, Locale.ENGLISH);

        // Assert
        assertTrue(result.contains("999"));
    }

    @Test
    void buildMessage_DifferentLocales_ReturnsMessage() {
        // Arrange
        FollowerEventDto event = FollowerEventDto.builder()
                .followerId(1L)
                .build();

        // Act
        String resultEn = messageBuilder.buildMessage(event, Locale.ENGLISH);
        String resultRu = messageBuilder.buildMessage(event, new Locale("ru"));

        // Assert
        assertNotNull(resultEn);
        assertNotNull(resultRu);
        assertEquals(resultEn, resultRu); // Currently same format
    }

    @Test
    void getInstance_ReturnsFollowerEventDtoClass() {
        // Act
        Class<?> result = messageBuilder.getInstance();

        // Assert
        assertEquals(FollowerEventDto.class, result);
    }

    @Test
    void buildMessage_WithNullLocale_StillReturnsMessage() {
        // Arrange
        FollowerEventDto event = FollowerEventDto.builder()
                .followerId(1L)
                .build();

        // Act
        String result = messageBuilder.buildMessage(event, null);

        // Assert
        assertNotNull(result);
    }
}