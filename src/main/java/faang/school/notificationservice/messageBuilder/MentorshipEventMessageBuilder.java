package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.dto.MentorshipEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MentorshipEventMessageBuilder implements MessageBuilder<MentorshipEventDto> {
    private final MessageSource messageSource;

    public String buildMessage(MentorshipEventDto event, UserDto userDto) {
        return messageSource.getMessage("mentorship.new", new Object[] {event.getRequesterId(), event.getCreatedAt()}, userDto.getLocale());
    }

    @Override
    public Class<MentorshipEventDto> supportsEventType() {
        return MentorshipEventDto.class;
    }
}
