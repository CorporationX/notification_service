package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.NotificationData;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectFollowerMessageBuilderTest {
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private ProjectFollowerMessageBuilder projectFollowerMessageBuilder;

    private ProjectFollowerEvent event;
    private NotificationData notificationData;

    @BeforeEach
    void setUp() {
        event = ProjectFollowerEvent.builder()
                .build();
        notificationData = NotificationData.builder()
                .follower("Igor")
                .build();
    }

    @Test
    public void whenGetInstanceThenGetProjectFollowerEventClass() {
        assertThat(projectFollowerMessageBuilder.getInstance()).isEqualTo(event.getClass());
    }

    @Test
    public void whenBuildMessageThenReturnMessage() {
        String message = "Igor followed for your project!";
        when(messageSource.getMessage(any(), any(), any())).thenReturn(message);
        String actual = projectFollowerMessageBuilder.buildMessage(event, Locale.UK, notificationData);
        assertThat(actual).isEqualTo(message);
    }
}