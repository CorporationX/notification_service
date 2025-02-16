package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private RecommendationEventMessageBuilder recommendationEventMessageBuilder;

    private RecommendationEvent recommendationEvent;
    private UserDto userDto;
    private Locale locale;

    @BeforeEach
    void setUp() {
        recommendationEvent = new RecommendationEvent(1L, 2L, 3L);
        userDto = new UserDto();
        userDto.setUsername("testUser");
        locale = Locale.ENGLISH;
    }

    @Test
    void testGetInstance() {
        Class<?> result = recommendationEventMessageBuilder.getInstance();

        assertEquals(RecommendationEvent.class, result);
    }

    @Test
    void testBuildMessage() {
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(messageSource.getMessage(
                eq("You received a recommendation request from "),
                eq(new Object[]{userDto.getUsername()}),
                eq(locale))
        ).thenReturn("You received a recommendation request from testUser");

        String result = recommendationEventMessageBuilder.buildMessage(recommendationEvent, locale);

        assertEquals("You received a recommendation request from testUser", result);
        verify(userServiceClient, times(1)).getUser(1L);
        verify(messageSource, times(1)).getMessage(
                eq("You received a recommendation request from "),
                eq(new Object[]{userDto.getUsername()}),
                eq(locale)
        );
    }
}