package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventStartDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventStartMessageBuilderTest {
    @InjectMocks
    private EventStartMessageBuilder eventStartMessageBuilder;
    @Mock
    private MessageSource messageSource;
    private EventStartDto event;
    private LocalDateTime eventDate;

    @BeforeEach
    void setUp() {
        event = EventStartDto.builder()
                .title("title")
                .notifiedAttendee(UserDto.builder()
                        .username("username")
                        .build())
                .build();
        String text = "Hi, " + event.getNotifiedAttendee().getUsername() + "!" +
                " Event: " + event.getTitle() + " is starting";

        when(messageSource.getMessage(
                "event.start",
                new Object[]{event.getNotifiedAttendee().getUsername(), event.getTitle()},
                Locale.getDefault()))
                .thenReturn(text);

        eventDate = LocalDateTime.of(2023, 1, 1, 0, 0);
    }

    @Test
    void buildMessage_shouldReturnMessageInADay() {
        LocalDateTime now = eventDate.minusDays(1);
        long tillStart = eventDate.toInstant(ZoneOffset.UTC).toEpochMilli() - now.toInstant(ZoneOffset.UTC).toEpochMilli();
        event.setTimeTillStart(tillStart);

        String actual = eventStartMessageBuilder.buildMessage(event, Locale.getDefault());
        String expected = "Hi, username! Event: title is starting in a day!";
        assertEquals(expected, actual);
    }

    @Test
    void buildMessage_shouldReturnMessageInFiveHours() {
        LocalDateTime now = eventDate.minusHours(5);
        long tillStart = eventDate.toInstant(ZoneOffset.UTC).toEpochMilli() - now.toInstant(ZoneOffset.UTC).toEpochMilli();
        event.setTimeTillStart(tillStart);

        String actual = eventStartMessageBuilder.buildMessage(event, Locale.getDefault());
        String expected = "Hi, username! Event: title is starting in five hours!";
        assertEquals(expected, actual);
    }

    @Test
    void buildMessage_shouldReturnMessageInOneHour() {
        LocalDateTime now = eventDate.minusHours(1);
        long tillStart = eventDate.toInstant(ZoneOffset.UTC).toEpochMilli() - now.toInstant(ZoneOffset.UTC).toEpochMilli();
        event.setTimeTillStart(tillStart);

        String actual = eventStartMessageBuilder.buildMessage(event, Locale.getDefault());
        String expected = "Hi, username! Event: title is starting in an hour!";
        assertEquals(expected, actual);
    }

    @Test
    void buildMessage_shouldReturnMessageInTenMinutes() {
        LocalDateTime now = eventDate.minusMinutes(10);
        long tillStart = eventDate.toInstant(ZoneOffset.UTC).toEpochMilli() - now.toInstant(ZoneOffset.UTC).toEpochMilli();
        event.setTimeTillStart(tillStart);

        String actual = eventStartMessageBuilder.buildMessage(event, Locale.getDefault());
        String expected = "Hi, username! Event: title is starting in ten minutes!";
        assertEquals(expected, actual);
    }

    @Test
    void buildMessage_shouldReturnMessageNow() {
        LocalDateTime now = eventDate;
        long tillStart = eventDate.toInstant(ZoneOffset.UTC).toEpochMilli() - now.toInstant(ZoneOffset.UTC).toEpochMilli();
        event.setTimeTillStart(tillStart);

        String actual = eventStartMessageBuilder.buildMessage(event, Locale.getDefault());
        String expected = "Hi, username! Event: title is starting now!";
        assertEquals(expected, actual);
    }
}