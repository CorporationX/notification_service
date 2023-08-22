package faang.school.notificationservice.messaging.builders;

import faang.school.notificationservice.messaging.events.ProfileViewEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ViewProfileMessageBuilderTest {

    @Spy
    private MessageSource messageSource;
    @InjectMocks
    private ViewProfileMessageBuilder viewProfileMessageBuilder;
    private final String PROPERTIES_FILE_NAME = "visitor.new";
    private final Long idVisitor = 10L;
    private final Long idVisited = 20L;
    private final Locale locale = Locale.ENGLISH;
    private ProfileViewEvent profileViewEvent;

    @BeforeEach
    public void setUp(){
        profileViewEvent = new ProfileViewEvent(idVisitor, idVisited);
    }

    @Test
    public void buildMessageCallsSourceMethodTest(){
        String expectedMessage = "User with ID 10 viewed your profile with ID 20";
        Mockito.when(messageSource.getMessage(PROPERTIES_FILE_NAME, new Object[]{idVisitor, idVisited}, locale))
                .thenReturn(String.format("User with ID %d viewed your profile with ID %d", idVisitor, idVisited));

        assertEquals(expectedMessage, viewProfileMessageBuilder.buildMessage(profileViewEvent, locale));
        Mockito.verify(messageSource, Mockito.times(1))
                .getMessage(PROPERTIES_FILE_NAME, new Object[]{idVisitor, idVisited}, locale);
    }
}