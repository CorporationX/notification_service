package faang.school.notificationservice.messaging;

import faang.school.notificationservice.config.MessageSourceConfig;
import faang.school.notificationservice.dto.publishable.LikeEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
@Import({MessageSourceConfig.class, LikeMessageBuilder.class})
@ExtendWith(SpringExtension.class)
public class LikeMessageBuilderTest2 {
    @Autowired
    private LikeMessageBuilder messageBuilder;
    private String receiverName = "Viet";
    private String actorName = "Gena";
    private Object[] args = new Object[]{receiverName, actorName};
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
    private String text = resourceBundle.getString("like.new");
    private LikeEvent event = new LikeEvent();
    @Test
    public void testGetInstance() {
        Class<?> result = messageBuilder.getInstance();

        assertEquals(LikeEvent.class, result);
    }

    @Test
    public void testBuildMessage() {
        String expected = MessageFormat.format(text, args);
        String result = messageBuilder.buildMessage(event, Locale.ENGLISH, args);

        assertEquals(expected, result);
    }
}
