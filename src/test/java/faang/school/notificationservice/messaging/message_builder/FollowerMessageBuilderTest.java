package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.client.service.UserServiceClient;
import faang.school.notificationservice.dto.redis.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerMessageBuilderTest {
    @InjectMocks
    FollowerMessageBuilder followerMessageBuilder;
    @Mock
    UserServiceClient userServiceClient;
    @Mock
    MessageSource messageSource;

    @Test
    public void testBuildMessage() {
        FollowerEvent event = FollowerEvent.builder()
                .followerId(1L)
                .build();
        Locale locale = Locale.getDefault();
        UserDto userDto = UserDto.builder().id(1L)
                .username("Name")
                .build();
        String expectedMessage = "Congrats! You've got a new follower - Name!";

        when(userServiceClient.getUser(event.getFollowerId())).thenReturn(userDto);
        when(messageSource.getMessage("follower.new", new String[]{"Name"}, locale)).thenReturn(expectedMessage);

        String actualMessage = followerMessageBuilder.buildMessage(event, locale);

        assertEquals(expectedMessage, actualMessage);
    }
}
