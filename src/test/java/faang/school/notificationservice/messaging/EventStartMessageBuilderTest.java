package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.EventTimeToStart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventStartMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private EventStartMessageBuilder eventStartMessageBuilder;

    @Test
    void getInstanceTest() {
        Class<?> result = eventStartMessageBuilder.supportEventType();

        assertEquals(EventStartEventDto.class, result);
    }

    @Test
    void eventStartNowTest() {
        Long eventId = 1L;
        Locale locale = Locale.ENGLISH;
        List<Long> participants = new ArrayList<>();
        EventStartEventDto eventDto = new EventStartEventDto(eventId, participants, EventTimeToStart.START_NOW);
        String message = "event." + EventTimeToStart.START_NOW;
        String messageToSend = "Event 1 start now!";
        when(messageSource.getMessage(message, new Object[]{eventId}, locale)).thenReturn(messageToSend);

        String result = eventStartMessageBuilder.buildMessage(eventDto, locale);

        assertEquals(messageToSend, result);
        verify(messageSource, times(1)).getMessage(message, new Object[]{eventId}, locale);
    }

    @Test
    void eventStartIn10MinTest() {
        Long eventId = 1L;
        Locale locale = Locale.ENGLISH;
        List<Long> participants = new ArrayList<>();
        EventStartEventDto eventDto = new EventStartEventDto(eventId, participants, EventTimeToStart.START_THROUGH_10_MIN);
        String message = "event." + EventTimeToStart.START_THROUGH_10_MIN;
        String messageToSend = "Event 1 start through 10 minutes!";
        when(messageSource.getMessage(message, new Object[]{eventId}, locale)).thenReturn(messageToSend);

        String result = eventStartMessageBuilder.buildMessage(eventDto, locale);

        assertEquals(messageToSend, result);
        verify(messageSource, times(1)).getMessage(message, new Object[]{eventId}, locale);
    }

    @Test
    void eventStartIn1HourTest() {
        Long eventId = 1L;
        Locale locale = Locale.ENGLISH;
        List<Long> participants = new ArrayList<>();
        EventStartEventDto eventDto = new EventStartEventDto(eventId, participants, EventTimeToStart.START_THROUGH_1_HOUR);
        String message = "event." + EventTimeToStart.START_THROUGH_1_HOUR;
        String messageToSend = "Event 1 start through 1 hour!";
        when(messageSource.getMessage(message, new Object[]{eventId}, locale)).thenReturn(messageToSend);

        String result = eventStartMessageBuilder.buildMessage(eventDto, locale);

        assertEquals(messageToSend, result);
        verify(messageSource, times(1)).getMessage(message, new Object[]{eventId}, locale);
    }

    @Test
    void eventStartIn5HoursTest() {
        Long eventId = 1L;
        Locale locale = Locale.ENGLISH;
        List<Long> participants = new ArrayList<>();
        EventStartEventDto eventDto = new EventStartEventDto(eventId, participants, EventTimeToStart.START_THROUGH_5_HOURS);
        String message = "event." + EventTimeToStart.START_THROUGH_5_HOURS;
        String messageToSend = "Event 1 start through 5 hours!";
        when(messageSource.getMessage(message, new Object[]{eventId}, locale)).thenReturn(messageToSend);

        String result = eventStartMessageBuilder.buildMessage(eventDto, locale);

        assertEquals(messageToSend, result);
        verify(messageSource, times(1)).getMessage(message, new Object[]{eventId}, locale);
    }

    @Test
    void eventStartIn1DayTest() {
        Long eventId = 1L;
        Locale locale = Locale.ENGLISH;
        List<Long> participants = new ArrayList<>();
        EventStartEventDto eventDto = new EventStartEventDto(eventId, participants, EventTimeToStart.START_THROUGH_1_DAY);
        String message = "event." + EventTimeToStart.START_THROUGH_1_DAY;
        String messageToSend = "Event 1 start through 1 day!";
        when(messageSource.getMessage(message, new Object[]{eventId}, locale)).thenReturn(messageToSend);

        String result = eventStartMessageBuilder.buildMessage(eventDto, locale);

        assertEquals(messageToSend, result);
        verify(messageSource, times(1)).getMessage(message, new Object[]{eventId}, locale);
    }
}
