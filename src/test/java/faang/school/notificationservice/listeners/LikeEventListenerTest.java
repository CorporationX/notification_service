package faang.school.notificationservice.listeners;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messages.LikeEventMessageBuilder;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private List<NotificationService> notificationServiceList;
    @Mock
    private List<MessageBuilder<LikeEvent>> messageBuilderList;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;
    @Mock
    private LikeEventMessageBuilder likeEventMessageBuilder;
    @Mock
    private Message message;
    @InjectMocks
    private LikeEventListener likeEventListener;

    private LikeEvent likeEventPost;
    private LikeEvent likeEventComment;
    private UserDto userDto;
    byte[] pattern;
    private Locale locale;
    private String sendMessage;

    @BeforeEach
    public void setUp() {
        likeEventListener = new LikeEventListener(objectMapper,
                                                  notificationServiceList,
                                                  messageBuilderList,
                                                  userServiceClient);

        userDto = UserDto.builder()
                .id(100L)
                .username("Ivan")
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();

        likeEventPost = LikeEvent.builder()
                .authorLikeId(userDto.getId())
                .authorPostId(200L)
                .postId(300L)
                .build();

        likeEventComment = LikeEvent.builder()
                .authorLikeId(userDto.getId())
                .authorCommentId(400L)
                .commentId(500L)
                .build();

        pattern = new byte[]{};
        locale = Locale.getDefault();
        sendMessage = "test";
    }


    // TODO: выдает ошибку No message constructor was found for the event: faang.school.notificationservice.dto.LikeEvent
//    @Test
//    @DisplayName("Checking that the method is called with the correct arguments and and sending notification")
//    void testOnMessage() throws IOException {
//        when(objectMapper.readValue(message.getBody(), LikeEvent.class)).thenReturn(likeEventPost);
//        when(userServiceClient.getUser(likeEventPost.getAuthorLikeId())).thenReturn(userDto);
//        when(likeEventMessageBuilder.buildMessage(likeEventPost, locale)).thenReturn(sendMessage);
//        when(userServiceClient.getUser(likeEventPost.getAuthorLikeId())).thenReturn(userDto);
//        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);
//
//        likeEventListener.onMessage(message, pattern);
//
//        verify(notificationService, times(1)).send(userDto, sendMessage);
//    }
}
