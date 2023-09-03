package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.skill.SkillOfferEvent;
import faang.school.notificationservice.notification.SkillOfferedNotificationSender;
import faang.school.notificationservice.util.JsonMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillOfferedEventListenerTest {
    @Mock
    JsonMapper jsonMapper;
    @Mock
    UserServiceClient userClient;
    @Mock
    SkillOfferedNotificationSender sender;
    @InjectMocks
    SkillOfferedEventListener listener;

    @Test
    @DisplayName("Successfully serializes SkillOfferEvent")
    void successfullySerializes() {
        Message message = getMessage();
        UserDto receiver = UserDto.builder()
                .id(21)
                .build();
        SkillOfferEvent event = SkillOfferEvent.builder()
                .senderId(12L)
                .receiverId(21L)
                .skillId(11L)
                .build();

        when(userClient.getUser(21))
                .thenReturn(receiver);
        when(jsonMapper.toObject(Arrays.toString(message.getBody()), SkillOfferEvent.class))
                .thenReturn(Optional.of(event));

        listener.onMessage(message, null);

        verify(jsonMapper)
                .toObject(Arrays.toString(message.getBody()), SkillOfferEvent.class);
        verify(sender)
                .send(event, receiver);
    }

    @ParameterizedTest
    @MethodSource("getWrongMessages")
    @DisplayName("Could not deserialize SkillOfferEvent")
    void couldNotDeserialize(Message message) {
        when(jsonMapper.toObject(Arrays.toString(message.getBody()), SkillOfferEvent.class))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> listener.onMessage(message, null));
        assertEquals("Could not deserialize message", exception.getMessage());
    }

    private static Stream<Arguments> getWrongMessages() {
        return Stream.of(
                Arguments.of(new DefaultMessage("".getBytes(), new byte[0])),
                Arguments.of(new DefaultMessage(new byte[0], new byte[0])),
                Arguments.of(new DefaultMessage("test".getBytes(), new byte[0])),
                Arguments.of(new DefaultMessage("test".getBytes(), """
                        {
                          "senderId": 12,
                          "receiverId": 21,
                        }
                        """.getBytes()
                )),
                Arguments.of(new DefaultMessage("test".getBytes(), """
                        {
                          "senderId": 12,
                          "skillId": 11
                        }
                        """.getBytes()
                )),
                Arguments.of(new DefaultMessage("test".getBytes(), """
                        {
                          "skillId": 11
                          "receiverId": 21,
                        }
                        """.getBytes()
                )),
                Arguments.of(new DefaultMessage("test".getBytes(), """
                        {
                          "senderId":,
                          "receiverId":,
                          "skillId":
                        }
                        """.getBytes()
                ))
        );
    }

    private Message getMessage() {
        return new DefaultMessage("test".getBytes(), """
                {
                  "senderId": 12,
                  "receiverId": 21,
                  "skillId": 11
                }
                """.getBytes());
    }
}