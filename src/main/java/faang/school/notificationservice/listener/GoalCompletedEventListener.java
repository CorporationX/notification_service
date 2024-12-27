package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.EventHandler;
import faang.school.notificationservice.event.GoalCompletedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GoalCompletedEventListener extends AbstractListener<GoalCompletedEvent> {
    private final UserFeignService userFeignService;

    public GoalCompletedEventListener(ObjectMapper objectMapper,
                                      List<EventHandler<GoalCompletedEvent>> eventHandlers,
                                      List<NotificationService> notificationServices,
                                      MessageBuilder<GoalCompletedEvent> messageBuilder,
                                      UserFeignService userFeignService) {
        super(objectMapper, eventHandlers, notificationServices, messageBuilder);
        this.userFeignService = userFeignService;
    }

    @Override
    protected void handleEvent(GoalCompletedEvent event) {
        UserContactsDto receiverDto = userFeignService.getUserContacts(event.getActorId());
        String message = createMessage(event);
        sendNotification(receiverDto, event, message);
    }

    @Override
    protected Class<GoalCompletedEvent> getEventType() {
        return GoalCompletedEvent.class;
    }
}
