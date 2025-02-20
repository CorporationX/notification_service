package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationEvent;
import faang.school.notificationservice.listener.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static faang.school.notificationservice.listener.EventType.EVENT_TYPE_RECOMMENDATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private RecommendationEventMessageBuilder recommendationEventMessageBuilder;

    private RecommendationEvent recommendationEvent;
    private UserDto userDto;
    private Locale locale;
    private String messageText;

    @BeforeEach
    void setUp() {
        recommendationEvent = new RecommendationEvent(1L, 2L, 3L);
        userDto = new UserDto();
        userDto.setUsername("testUser");
        locale = Locale.ENGLISH;
        messageText = "You have received a recommendation request from a user testUser";
    }

    @Test
    void testGetEventTypeSuccess() {
        EventType eventType = recommendationEventMessageBuilder.getEventType();

        assertEquals(EVENT_TYPE_RECOMMENDATION, eventType);
    }

    @Test
    void testBuildMessageSuccess() {
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(messageSource.getMessage(
                eq("recommendation.new"),
                eq(new Object[]{userDto.getUsername()}),
                eq(locale))
        ).thenReturn(messageText);

        String result = recommendationEventMessageBuilder.buildMessage(recommendationEvent, locale);

        assertEquals(messageText, result);
        verify(userServiceClient, times(1)).getUser(1L);
        verify(messageSource, times(1)).getMessage(
                eq("recommendation.new"),
                eq(new Object[]{userDto.getUsername()}),
                eq(locale)
        );
    }
}