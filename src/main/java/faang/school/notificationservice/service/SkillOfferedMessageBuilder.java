package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.skill.SkillOfferEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class SkillOfferedMessageBuilder implements MessageBuilder<SkillOfferEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient client;

    @Override
    public String buildMessage(SkillOfferEvent event, Locale locale) {
        Object[] args = buildArgs(event);
        return messageSource.getMessage("skill.offered", args, locale);
    }

    private Object[] buildArgs(SkillOfferEvent event) {
        Object[] args = new Object[2];
        args[0] = client.getUserDtoForNotification(event.getSenderId()).getUsername();
        args[1] = client.getSkillById(event.getSkillId()).getTitle();
        return args;
    }

}
