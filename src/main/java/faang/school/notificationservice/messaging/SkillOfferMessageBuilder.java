package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MessageCode;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.SkillOfferEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SkillOfferMessageBuilder implements MessageBuilder<SkillOfferEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return SkillOfferEvent.class;
    }

    @Override
    public String buildMessage(SkillOfferEvent event, Locale locale) {
        UserDto requester = userServiceClient.getUser(event.requesterId());
        Object[] args = new Object[]{requester.getUsername(), event.skillTitle()};
        return messageSource.getMessage(MessageCode.SKILL_OFFER.getCode(), args, locale);
    }
}
