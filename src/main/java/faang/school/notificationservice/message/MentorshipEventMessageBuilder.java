package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.event.MentorshipEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.builder.MessageBuilder;
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
    public Class<MentorshipEventDto> getEventType() {
        return MentorshipEventDto.class;
    }
}
