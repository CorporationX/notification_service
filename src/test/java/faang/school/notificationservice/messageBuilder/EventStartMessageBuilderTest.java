package faang.school.notificationservice.messageBuilder;


import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.user.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

class EventStartMessageBuilderTest {
    private final EventStartMessageBuilder eventStartMessageBuilder = new EventStartMessageBuilder();

    @Test
    void buildMessage_Test() {
        EventDto event = new EventDto();
        event.setId(1L);
        event.setDescription("SomeDescription");
        event.setLocation("SomeLocation");
        event.setStartDate(LocalDateTime.now().plusMinutes(11));
        event.setTitle("SomeTitle");
        event.setEndDate(LocalDateTime.now().plusMinutes(2));
        UserDto user = new UserDto();
        user.setId(1L);
        user.setEmail("some@email.com");
        user.setPhone("somePhone");
        user.setUsername("userName");
        user.setPreference(UserDto.PreferredContact.SMS);
        event.setUserDto(user);

        String result = eventStartMessageBuilder.buildMessage(event, String.valueOf(TimeUnit.MINUTES.toMinutes(10)));
        System.out.println(result);
        String expected = "Hello, userName.\nEvent named \"SomeTitle\" is starting\nLocation: SomeLocation\nDescription: SomeDescription\n";
        Assertions.assertEquals(expected, result);

        assertEquals(expected, result);
    }
}