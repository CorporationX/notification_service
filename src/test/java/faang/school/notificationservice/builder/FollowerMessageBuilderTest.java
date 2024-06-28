package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowerMessageBuilderTest {
    @InjectMocks
    private FollowerMessageBuilder followerMessageBuilder;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;

    @Test
    @DisplayName("Correct build message.")
    public void testBuildMessage() {
        String expectedMessage = "Congrats! You've got a new follower!";

        FollowerEvent event = FollowerEvent.builder()
                .follower(1L)
                .followee(2L)
                .build();

        UserDto follower = UserDto.builder()
                .id(1L)
                .username("follower")
                .email("follower@gmail.com")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        UserDto followee = UserDto.builder()
                .id(2L)
                .username("followee")
                .email("followee@gmail.com")
                .preference(UserDto.PreferredContact.EMAIL)
                .preferredLocale("ENGLISH")
                .build();

        when(userServiceClient.getUser(1L)).thenReturn(follower);
        when(userServiceClient.getUser(2L)).thenReturn(followee);
        when(messageSource.getMessage(eq("follower.new"), any(), eq(Locale.ENGLISH))).thenReturn(expectedMessage);

        String actualMessage = followerMessageBuilder.buildMessage(event);

        assertEquals(expectedMessage, actualMessage);
        verify(userServiceClient, times(1)).getUser(1L);
        verify(userServiceClient, times(1)).getUser(2L);
        verify(messageSource, times(1)).getMessage(eq("follower.new"), any(), eq(Locale.ENGLISH));
    }
}
