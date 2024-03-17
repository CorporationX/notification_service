package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerMessageBuilderTest {
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private FollowerMessageBuilder followerMessageBuilder;

    @Test
    void shouldBuildMessage() {
        UserDto userDto = new UserDto();
        userDto.setUsername("TestName");
        FollowerEventDto followerEventDto = new FollowerEventDto();
        followerEventDto.setFollowerId(1L);
        when(userServiceClient.getUser(Mockito.anyLong())).thenReturn(userDto);
        followerMessageBuilder.buildMessage(new FollowerEventDto());
        verify(messageSource).getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }
}