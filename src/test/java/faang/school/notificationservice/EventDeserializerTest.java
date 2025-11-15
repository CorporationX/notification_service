package faang.school.notificationservice;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.exception.EventDeserializationException;
import faang.school.notificationservice.service.EventDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventDeserializerTest {

    @Mock
    private ObjectMapper objectMapper;

    private EventDeserializer eventDeserializer;

    @BeforeEach
    void setUp() {
        eventDeserializer = new EventDeserializer(objectMapper);
    }

    @Test
    void deserialize_ValidJson_ReturnsEvent() throws Exception {
        // Arrange
        String json = "{\"follower_id\":1,\"followee_id\":2}";
        FollowerEventDto expectedEvent = FollowerEventDto.builder()
                .followerId(1L)
                .followeeId(2L)
                .build();

        when(objectMapper.readValue(json, FollowerEventDto.class))
                .thenReturn(expectedEvent);

        // Act
        FollowerEventDto result = eventDeserializer.deserialize(json, FollowerEventDto.class);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getFollowerId());
        assertEquals(2L, result.getFolloweeId());
        verify(objectMapper).readValue(json, FollowerEventDto.class);
    }

    @Test
    void deserialize_InvalidJson_ThrowsException() throws Exception {
        // Arrange
        String invalidJson = "invalid json";
        when(objectMapper.readValue(eq(invalidJson), any(Class.class)))
                .thenThrow(new RuntimeException("Parse error"));

        // Act & Assert
        assertThrows(EventDeserializationException.class,
                () -> eventDeserializer.deserialize(invalidJson, FollowerEventDto.class));

        verify(objectMapper).readValue(invalidJson, FollowerEventDto.class);
    }

    @Test
    void deserialize_NullJson_ThrowsException() throws Exception {
        // Arrange
        when(objectMapper.readValue((JsonParser) eq(null), any(Class.class)))
                .thenThrow(new RuntimeException("Null input"));

        // Act & Assert
        assertThrows(EventDeserializationException.class,
                () -> eventDeserializer.deserialize(null, FollowerEventDto.class));
    }
}