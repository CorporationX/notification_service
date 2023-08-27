package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.FollowerEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.mockito.Mockito;

import java.util.Locale;

@ExtendWith(MockitoExtension.class)
class FollowEventMessageBuilderTest {

    @InjectMocks
    FollowEventMessageBuilder followEventMessageBuilder;
    @Mock
    MessageSource messageSource;

    @Test
    void testBuildMessage() {
        String userName = "User";
        UserDto userDto = UserDto.builder().username(userName).build();
        FollowerEventDto followerEventDto = FollowerEventDto.builder().build();
        followEventMessageBuilder.buildMessage(userDto, followerEventDto);
        Mockito.verify(messageSource).getMessage("follower.new", new Object[]{userName}, null);
    }
}