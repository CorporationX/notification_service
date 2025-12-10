package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.like.UnlikeEventDto;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@ToString
@Component
public class UnlikeMessageBuilder implements MessageBuilder<UnlikeEventDto> {

    @Override
    public Class<UnlikeEventDto> getInstance() {
        return UnlikeEventDto.class;
    }

    @Override
    public String buildMessage(UnlikeEventDto event, Locale locale) {
        ResourceBundle bundle =ResourceBundle.getBundle("messages", locale);
        return String.format(bundle.getString("like.unlike"), event.likeAuthorId(), event.postId());
    }
}
