package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ProfileViewMessageBuilder extends MessageBuilder<ProfileViewEvent> {

    public ProfileViewMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Class<ProfileViewEvent> getInstance() {
        return ProfileViewEvent.class;
    }

    @Override
    public String buildMessage(UserDto userDto, ProfileViewEvent event) {
        return messageSource.getMessage("follower.new", null, userDto.getLocale());
    }
}
