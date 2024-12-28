package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.EventStartEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventStartEventMessageBuilderTest {

    private static final String EXPECTED_MESSAGE = "Event will start at 00:00! Be ready!";

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private EventStartMessageBuilder eventStartMessageBuilder;

    private EventStartEvent event;
    private Locale locale;

    @BeforeEach
    void setUp() {
        event = EventStartEvent.builder()
                .eventId(1L)
                .eventTitle("Test Event")
                .eventStartTime(LocalDateTime.now())
                .attendeesIds(List.of(1L, 2L))
                .build();
        locale = Locale.getDefault();
    }

    @Test
    @DisplayName("getInstance return success")
    void testGetInstanceSuccess() {
        EventStartMessageBuilder messageBuilder = new EventStartMessageBuilder(null);

        Class<EventStartEvent> result = messageBuilder.getInstance();

        assertNotNull(result);
    }

    @Test
    @DisplayName("Build message test")
    void testBuildMessage() {
        when(messageSource.getMessage(
                eq("event.started"),
                any(Object[].class),
                eq(locale)
        )).thenReturn(EXPECTED_MESSAGE);

        String result = eventStartMessageBuilder.buildMessage(event, locale);

        verify(messageSource, times(1)).getMessage(eq("event.started"), any(Object[].class), eq(locale));
        assertEquals(EXPECTED_MESSAGE, result);
    }
}
