package faang.school.notificationservice.builder;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder{
    private final MessageSource messageSource;
    public String buildMessage(String followerName) {
        return messageSource.getMessage("follower.new", new Object[]{followerName}, Locale.US);
    }
}
