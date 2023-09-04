package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.CommentEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private CommentEventMessageBuilder messageBuilder;

    @Test
    void buildMessage_shouldReturnFormattedMessage() {
        CommentEventDto event = CommentEventDto.builder()
                .idComment(1L)
                .postId(1L)
                .postAuthorId(1L)
                .contentComment("Content")
                .authorIdComment(2L)
                .build();

        UserDto userPostAuthor = UserDto.builder().id(1L).username("Name").build();
        UserDto userAuthorComment = UserDto.builder().id(2L).username("Name").build();

        when(userServiceClient.getUser(1L)).thenReturn(userPostAuthor);
        when(userServiceClient.getUser(2L)).thenReturn(userAuthorComment);
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Formatted message");

        String result = messageBuilder.buildMessage(event, Locale.getDefault());

        verify(userServiceClient, times(2)).getUser(anyLong());
        verify(messageSource).getMessage(eq("comment.start"), any(Object[].class), any(Locale.class));
        assertNotNull(result);
    }
}