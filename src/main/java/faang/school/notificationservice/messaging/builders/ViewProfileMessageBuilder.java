package faang.school.notificationservice.messaging.builders;

import faang.school.notificationservice.messaging.events.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ViewProfileMessageBuilder implements MessageBuilder<ProfileViewEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(Locale local, ProfileViewEvent event) {
        return messageSource.getMessage("visitor.new",
                new Object[]{event.getIdVisitor(), event.getIdVisited()}, local);
    }

}
