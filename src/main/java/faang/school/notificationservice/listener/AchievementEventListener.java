package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.AchievementEvent;
import faang.school.notificationservice.event.EventHandler;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AchievementEventListener extends AbstractListener<AchievementEvent> {
    private final UserFeignService userFeignService;

    public AchievementEventListener(ObjectMapper objectMapper,
                                    List<EventHandler<AchievementEvent>> eventHandlers,
                                    List<NotificationService> notificationServices,
                                    MessageBuilder<AchievementEvent> messageBuilder,
                                    UserFeignService userFeignService) {
        super(objectMapper, eventHandlers, notificationServices, messageBuilder);
        this.userFeignService = userFeignService;
    }


    @Override
    protected void handleEvent(AchievementEvent event) {
        UserContactsDto receiverDto = userFeignService.getUserContacts(event.getUserId());
        String message = createMessage(event);
        sendNotification(receiverDto, event, message);
    }

    @Override
    protected Class<AchievementEvent> getEventType() {
        return AchievementEvent.class;
    }
}
