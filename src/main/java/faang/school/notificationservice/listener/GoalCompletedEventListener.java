package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.GoalCompletedMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {
    private final GoalCompletedMessageBuilder messageBuilders;
    private final List<NotificationService> notificationServices;
    private final UserServiceClient userServiceClient;
    public GoalCompletedEventListener(GoalCompletedMessageBuilder messageBuilders,
                                      List<NotificationService> notificationServices,
                                      UserServiceClient userServiceClient) {
        super(GoalCompletedEvent.class);
        this.messageBuilders = messageBuilders;
        this.notificationServices = notificationServices;
        this.userServiceClient = userServiceClient;
    }

    @Override
    protected void processEvent(GoalCompletedEvent event) {
        UserDto userDto = userServiceClient.getUser(event.getUserId());
        UserDto.PreferredContact preference = userDto.getPreference();
        String message = messageBuilders.buildMessage(event, userDto.getUsername());

        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(preference))
                .findFirst()
                .ifPresent(service -> service.send(userDto, message));
    }
}
