package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.comment.CommentEvent;
import faang.school.notificationservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMessageBuilderTest {

    @InjectMocks
    private CommentMessageBuilder commentMessageBuilder;

    @Mock
    private MessageSource messageSource;
    @Mock
    private UserService userService;

    @Test
    void testGetInstance() {
        Class<CommentEvent> expected = CommentEvent.class;
        Class<CommentEvent> result = commentMessageBuilder.getInstance();

        assertEquals(expected, result);
    }

    @Test
    void testBuildMessage() {
        String expected = "Message";
        CommentEvent commentEvent = new CommentEvent();
        commentEvent.setAuthorId(1);

        UserDto userDto = new UserDto();
        userDto.setUsername("Username");

        when(userService.fetchUser(1L)).thenReturn(userDto);
        when(messageSource.getMessage(eq("comment.new"), any(), eq(Locale.US))).thenReturn("Message");

        String result = commentMessageBuilder.buildMessage(commentEvent, Locale.US);

        verify(userService).fetchUser(1L);
        verify(messageSource).getMessage(eq("comment.new"), any(), eq(Locale.US));
        assertEquals(expected, result);
    }
}