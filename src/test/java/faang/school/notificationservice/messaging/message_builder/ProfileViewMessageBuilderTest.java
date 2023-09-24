package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.client.service.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.redis.ProfileViewEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(value = {MockitoExtension.class})
public class ProfileViewMessageBuilderTest {
    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;

    @Test
    public void testBuildMessage() {
        ProfileViewEvent event = ProfileViewEvent.builder()
                .userId(1L)
                .profileViewedId(2L)
                .date(LocalDateTime.now())
                .build();
        UserDto userDto = UserDto.builder().id(1L)
                .username("Name")
                .build();
        Locale locale = Locale.ENGLISH;
        String msg = "Your profile has been viewed by a user by id 1";

        when(userServiceClient.getUser(event.getProfileViewedId())).thenReturn(userDto);
        when(messageSource.getMessage(eq("profile_view.new"), any(), eq(locale)))
                .thenReturn(msg);

        ProfileViewMessageBuilder profileViewMessageBuilder = new ProfileViewMessageBuilder(userServiceClient, messageSource);
        String result = profileViewMessageBuilder.buildMessage(event, locale);
        assertEquals(msg, result);
    }
}