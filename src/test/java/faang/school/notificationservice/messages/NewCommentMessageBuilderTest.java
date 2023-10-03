package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEventDto;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class NewCommentMessageBuilderTest {
    private final Locale locale = Locale.ENGLISH;
    private final String propertiesFileName = "comment.new";
    @Spy
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private NewCommentMessageBuilder commentMessageBuilder;

    private CommentEventDto commentEventDto;

    private UserDto commentatorUser;


    @BeforeEach
    void setUp() {
        commentEventDto = CommentEventDto.builder()
                .commentId(1L)
                .authorCommentId(1L)
                .authorPostId(2L)
                .content("Test comment")
                .build();

        commentatorUser = UserDto.builder()
                .id(1L)
                .username("Username 1")
                .build();
    }

    @Test
    void testBuildMessage() {
        String expectedMessage = "User Username 1 left a comment under your post: Test comment";

        Mockito.when(userServiceClient.getUserInternal(1L)).thenReturn(commentatorUser);
        String commentatorName = commentatorUser.username();
        String content = commentEventDto.getContent();
        Mockito.when(messageSource.getMessage(propertiesFileName, new Object[]{commentatorName, content}, locale))
                .thenReturn(String.format("User %s left a comment under your post: %s", commentatorName, content));

        String resultMessage = commentMessageBuilder.buildMessage(commentEventDto, locale);

        assertEquals(expectedMessage, resultMessage);
        Mockito.verify(messageSource, Mockito.times(1))
                .getMessage(propertiesFileName, new Object[]{commentatorName, content}, locale);
    }
}