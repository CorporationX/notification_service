package faang.school.notificationservice.message.builder;

import faang.school.notificationservice.dto.ProfileViewedEventDto;
import faang.school.notificationservice.messaging.ProfileViewedMessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageBuilderTest {
    private MessageSource messageSource;
    private ProfileViewedMessageBuilder builder;

    @BeforeEach
    void setup() {
        messageSource = mock(MessageSource.class);
        builder = new ProfileViewedMessageBuilder(messageSource);
    }

    @Test
    void getInstance_returnsProfileViewedEventDtoClass() {
        assertEquals(ProfileViewedEventDto.class, builder.getInstance());
    }

    @Test
    void buildMessage_callsMessageSourceWithCorrectArgs() {
        ProfileViewedEventDto event = new ProfileViewedEventDto();
        event.setViewedId(100L);
        event.setViewerName("Alice");
        event.setViewerId(200L);

        Locale locale = Locale.ENGLISH;
        String expectedMessage = "User Alice viewed profile 100";

        when(messageSource.getMessage(anyString(), any(), eq(locale))).thenReturn(expectedMessage);

        String actualMessage = builder.buildMessage(event, locale);

        assertEquals(expectedMessage, actualMessage);

        ArgumentCaptor<Object[]> argsCaptor = ArgumentCaptor.forClass(Object[].class);
        verify(messageSource).getMessage(eq("profile.viewed"), argsCaptor.capture(), eq(locale));

        Object[] capturedArgs = argsCaptor.getValue();
        assertEquals(3, capturedArgs.length);
        assertEquals(event.getViewedId(), capturedArgs[0]);
        assertEquals(event.getViewerName(), capturedArgs[1]);
        assertEquals(event.getViewerId(), capturedArgs[2]);
    }
}
