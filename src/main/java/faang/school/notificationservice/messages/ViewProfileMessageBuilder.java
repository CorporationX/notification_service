package faang.school.notificationservice.messages;

import faang.school.notificationservice.dto.ProfileViewEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ViewProfileMessageBuilder<T> implements MessageBuilder<ProfileViewEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(ProfileViewEventDto event, Locale locale) {
        return messageSource.getMessage("visitor.new",
                new Object[]{event.getIdVisitor(), event.getIdVisited()}, locale);
    }

    @Override
    public boolean supportsEventType(ProfileViewEventDto eventType) {
        return eventType.getClass() == ProfileViewEventDto.class;
    }
}
