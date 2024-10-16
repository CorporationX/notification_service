package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.messaging.RecommendationRequestedEventMessageBuilder;
import faang.school.notificationservice.model.event.RecommendationRequestedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationRequestedEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient client;

    @InjectMocks
    private RecommendationRequestedEventMessageBuilder builder;

    @Test
    void testBuilderOk(){
        RecommendationRequestedEvent event = RecommendationRequestedEvent.builder().build();

        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("hello there");
        when(client.getUser(anyLong())).thenReturn(UserDto.builder().build());

        assertEquals("hello there", builder.buildMessage(event, Locale.getDefault()));
    }
}
