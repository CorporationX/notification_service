package faang.school.notificationservice.messageBuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.mockito.Mockito;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FollowEventMessageBuilderTest {

    @InjectMocks
    FollowEventMessageBuilder followEventMessageBuilder;
    @Mock
    MessageSource messageSource;

    @Test
    void testBuildMessage() {
        Locale locale = new Locale("ENGLISH");
        String userName = "User";
        followEventMessageBuilder.buildMessage(locale, userName);
        Mockito.verify(messageSource).getMessage("follower.new", new Object[]{userName}, locale);
    }
}