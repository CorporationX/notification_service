package faang.school.notificationservice.eventlistener.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.subscription.SubscriptionEventDto;
import faang.school.notificationservice.messaging.subscription.UnfollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class UnfollowerEventListener extends AbstractSubscriptionListener {

    private final UnfollowerMessageBuilder messageBuilder;

    public UnfollowerEventListener(ObjectMapper objectMapper,
                                   UserServiceClient userServiceClient,
                                   List<NotificationService> notificationServices,
                                   UnfollowerMessageBuilder messageBuilder) {
        super(objectMapper, userServiceClient, notificationServices);
        this.messageBuilder = messageBuilder;
    }

    @Override
    protected String buildMessage(SubscriptionEventDto eventDto, Locale locale) {
        return messageBuilder.buildMessage(eventDto, locale);
    }

    @Override
    protected long getUserId(SubscriptionEventDto eventDto) {
        return eventDto.getFolloweeId();
    }
}
