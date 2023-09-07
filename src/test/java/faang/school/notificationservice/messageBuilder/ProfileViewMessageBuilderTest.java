package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.ProfileViewEventDto;
import faang.school.notificationservice.dto.user.UserNameDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileViewMessageBuilderTest {
    @InjectMocks
    ProfileViewMessageBuilder builder;
    @Mock
    UserServiceClient userServiceClient;

    @Test
    void buildMessage_Test() {
        UserNameDto user1 = UserNameDto.builder().username("user1").build();
        UserNameDto user2 = UserNameDto.builder().username("user2").build();
        ProfileViewEventDto event = ProfileViewEventDto.builder().profileOwnerId(1L).viewerId(2L).build();
        when(userServiceClient.getUserName(event.getProfileOwnerId())).thenReturn(user1);
        when(userServiceClient.getUserName(event.getViewerId())).thenReturn(user2);

        String result = builder.buildMessage(event, "eng");

        assertEquals("Hello, user1. Your profile was recently viewed by \"user2\" Best, XCorporation", result.trim());
    }
}