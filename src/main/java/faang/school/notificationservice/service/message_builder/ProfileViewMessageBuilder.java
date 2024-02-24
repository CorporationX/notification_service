package faang.school.notificationservice.service.message_builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ProfileViewMessageBuilder implements MessageBuilder<ProfileViewEvent> {
    @Override
    public String buildMessage(ProfileViewEvent event, Locale locale) {
        return event.getUserId() + " ProfileViewEvent example message";
    }

    @Override
    public long getReceiverId(ProfileViewEvent event) {
        return event.getUserId();
    }

    @Override
    public Class<ProfileViewEvent> eventType() {
        return ProfileViewEvent.class;
    }
}