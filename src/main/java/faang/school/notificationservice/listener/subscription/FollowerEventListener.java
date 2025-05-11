package faang.school.notificationservice.listener.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.subscription.SubscriptionEventDto;
import faang.school.notificationservice.messaging.subscription.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class FollowerEventListener extends AbstractSubscriptionListener {

    private final FollowerMessageBuilder messageBuilder;

    public FollowerEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 FollowerMessageBuilder messageBuilder) {
        super(objectMapper, userServiceClient, notificationServices);
        this.messageBuilder = messageBuilder;
    }

    @Override
    public String buildMessage(SubscriptionEventDto eventDto, Locale locale) {
        return messageBuilder.buildMessage(eventDto, locale);
    }

    @Override
    public long getUserId(SubscriptionEventDto eventDto) {
        return eventDto.getFolloweeId();
    }
}
