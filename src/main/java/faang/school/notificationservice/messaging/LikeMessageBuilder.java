package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.like.LikeEventDto;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@ToString
@Component
public class LikeMessageBuilder implements MessageBuilder<LikeEventDto> {

    @Override
    public Class<LikeEventDto> getInstance() {
        return LikeEventDto.class;
    }

    @Override
    public String buildMessage(LikeEventDto event, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return String.format(bundle.getString("like.new"), event.likeAuthorId(), event.postId());
    }
}
