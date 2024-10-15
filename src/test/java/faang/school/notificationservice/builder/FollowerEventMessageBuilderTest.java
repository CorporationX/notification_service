package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.messaging.FollowerEventMessageBuilder;
import faang.school.notificationservice.model.event.FollowerEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FollowerEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient serviceClient;

    @InjectMocks
    private FollowerEventMessageBuilder builder;

    @Test
    void testBuilderOk(){
        UserDto user = UserDto.builder()
                .username("pups")
                .build();
        when(serviceClient.getUser(anyLong())).thenReturn(user);
        FollowerEventDto event = FollowerEventDto.builder().build();

        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("hello there");

        assertEquals("hello there", builder.buildMessage(event, Locale.getDefault()));
    }
}
