package faang.school.notificationservice.builder;

import faang.school.notificationservice.messaging.ProjectFollowerMessageBuilder;
import faang.school.notificationservice.model.event.ProjectFollowerEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProjectFollowerMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ProjectFollowerMessageBuilder builder;

    @Test
    void testBuilderOk(){
        ProjectFollowerEvent event = ProjectFollowerEvent.builder().build();

        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("hello there");

        assertEquals("hello there", builder.buildMessage(event, Locale.getDefault()));
    }
}
