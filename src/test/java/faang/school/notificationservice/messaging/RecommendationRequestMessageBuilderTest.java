package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationRequestEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationRequestMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private RecommendationRequestMessageBuilder messageBuilder;

    private RecommendationRequestEvent recommendationRequestEvent;
    private UserDto author;
    private Locale locale;

    @BeforeEach
    void setUp() {
        author = new UserDto();
        author.setUsername("testUser");

        recommendationRequestEvent = new RecommendationRequestEvent();
        recommendationRequestEvent.setAuthor(author);

        locale = Locale.getDefault();
    }

    @Test
    void testSupportsEventType() {
        assertEquals(RecommendationRequestEvent.class, messageBuilder.supportsEventType());
    }

    @Test
    void testBuildMessage() {
        String expectedMessage = "User testUser has requested a recommendation from you.";
        Object[] expectedArgs = new Object[]{author.getUsername()};

        when(messageSource.getMessage("recommendation.request", expectedArgs, locale))
                .thenReturn(expectedMessage);

        String actualMessage = messageBuilder.buildMessage(recommendationRequestEvent, locale);

        assertEquals(expectedMessage, actualMessage);
    }
}