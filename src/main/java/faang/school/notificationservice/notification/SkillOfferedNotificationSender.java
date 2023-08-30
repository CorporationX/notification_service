package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.skill.SkillOfferEvent;
import faang.school.notificationservice.service.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkillOfferedNotificationSender extends NotificationSender<SkillOfferEvent> {
    public SkillOfferedNotificationSender(MessageBuilder<SkillOfferEvent> messageBuilder, List<NotificationService> services) {
        super(messageBuilder, services);
    }
}
