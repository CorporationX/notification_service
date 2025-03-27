package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.dto.PostShortContentDto;
import faang.school.notificationservice.dto.UserNotificationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

@ExtendWith(MockitoExtension.class)
public class LikeEventMessageBuilderTest {
    @Mock
    private  MessageSource messageSource;
    @Mock
    public  UserServiceClient userServiceClient;
    @Mock
    public  PostServiceClient postServiceClient;
    @InjectMocks
    public LikeEventMessageBuilder likeEventMessageBuilder;

    private final String postLikeParameter = "postlike.new";
    private  LikePostEvent event = new LikePostEvent();
    Locale locale = new Locale("en");
    UserNotificationDto userNotificationDtoAuthor = new UserNotificationDto();
    UserNotificationDto userNotificationDtoUser = new UserNotificationDto();
    PostShortContentDto postShortContentDto = new PostShortContentDto();

    @BeforeEach
    void setup() {
        event.setLikeUserId(1);
        event.setPostAuthorId(2);
        event.setPostId(3);

        userNotificationDtoAuthor.setUsername("Author");
        userNotificationDtoUser.setUsername("User");
        postShortContentDto.setShortContent("Short content");
    }

    @Test
    void testBuildMessageIsSuccessful() {
        Mockito.when(userServiceClient.getNotificationUser(event.getLikeUserId()))
                .thenReturn(userNotificationDtoUser);
        Mockito.when(userServiceClient.getNotificationUser(event.getPostAuthorId()))
                .thenReturn(userNotificationDtoAuthor);
        Mockito.when(postServiceClient.getShortenedPost(event.getPostId()))
                .thenReturn(postShortContentDto);

        likeEventMessageBuilder.buildMessage(event, locale);
        Mockito.verify(messageSource, Mockito.times(1)).getMessage(
                postLikeParameter,
                new Object[] { userNotificationDtoAuthor.getUsername(),
                        userNotificationDtoUser.getUsername(),
                        postShortContentDto.getShortContent()},
                locale);
    }

}
