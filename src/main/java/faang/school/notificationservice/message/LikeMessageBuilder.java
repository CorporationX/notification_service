package faang.school.notificationservice.message;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.message.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        String username = userServiceClient.getUser(event.getLikingUserId()).getUsername();
       return messageSource.getMessage("like.new", new Object[]{username}, locale);
    }
}
