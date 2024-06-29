package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.CommentMessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentMessageBuilderTest {
    @InjectMocks
    private CommentMessageBuilder commentMessageBuilder;

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @Test
    void testBuildMessage() {
        int authorId = 123;
        String expectedMessage = "New comment from testUser";
        UserDto testDto = UserDto.builder().username("name").build();

        CommentEventDto commentEventDto = new CommentEventDto();
        commentEventDto.setAuthorId(authorId);

        when(userServiceClient.getUser(authorId)).thenReturn(testDto);
        when(messageSource.getMessage(eq("comment.new"), any(Object[].class), eq(Locale.ENGLISH))).thenReturn(expectedMessage);

        String actualMessage = commentMessageBuilder.buildMessage(commentEventDto, Locale.ENGLISH);

        assertEquals(expectedMessage, actualMessage);
    }
}
