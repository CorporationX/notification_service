package faang.school.notificationservice.messages.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.LikeEventDto;
import faang.school.notificationservice.dto.user.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeMessageBuilderTest {
    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;
    @InjectMocks
    private LikeMessageBuilder likeMessageBuilder;

    @Test
    void builderMessage_ShouldReturnExpectedMessage() {
        UserDto userDto = UserDto.builder()
                .username("TestUser")
                .build();

        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class)))
                .thenReturn("Expected message");

        LikeEventDto likeEventDto = LikeEventDto.builder()
                .authorId(1L)
                .build();

        String message = likeMessageBuilder.builderMessage(likeEventDto, Locale.ENGLISH);

        assertEquals("Expected message", message);
    }
}