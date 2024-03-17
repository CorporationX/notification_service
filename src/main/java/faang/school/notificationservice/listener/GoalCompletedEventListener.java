package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.dto.GoalDto;
import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {

    public GoalCompletedEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                           List<NotificationService> notificationServices, List<MessageBuilder> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        GoalCompletedEvent event = getEvent(message, GoalCompletedEvent.class);
        log.info("Start processing an incoming event - {}", event);

        UserDto userReceiver = userServiceClient.getUser(event.getUserId());
        GoalDto goal = userServiceClient.getGoalById(event.getGoalId());
        event.setUserId(userReceiver.getId());
        event.setGoalId(goal.getId());
        String messageToSend = getMessage(event, Locale.getDefault());
        sendNotification(userReceiver, messageToSend);
        log.info("The message: {} has been sent to {}", messageToSend, userReceiver.getUsername());
    }
}
