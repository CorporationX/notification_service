package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.message.FollowerMessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.mockito.Mockito;

import java.util.Locale;

@ExtendWith(MockitoExtension.class)
class FollowEventMessageBuilderTest {

    @InjectMocks
    FollowerMessageBuilder followerMessageBuilder;
    @Mock
    MessageSource messageSource;

//    @Test
//    void testBuildMessage() {
//        Locale locale = new Locale("ENGLISH");
//        String userName = "User";
//        followerMessageBuilder.buildMessage(locale, userName);
//        Mockito.verify(messageSource).getMessage("follower.new", new Object[]{userName}, locale);
//    }
}