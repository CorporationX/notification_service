package faang.school.notificationservice.listener.achievement;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.event.achievement.AchievementEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Добавлено для примера
 * */

@Component
public class AchievementListener extends AbstractEventListener<AchievementEvent> {

    private final UserServiceClient userServiceClient;

    AchievementListener(
            List<NotificationService> notificationServices,
            ObjectMapper mapper,
            MessageBuilder<AchievementEvent> messageBuilder,
            UserServiceClient userServiceClient
    ) {
        super(notificationServices, mapper, messageBuilder, AchievementEvent.class);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public List<UserDto> getNotifiedUsers(AchievementEvent event) {

        UserDto notifiedUser = userServiceClient.getUser(
                event.getUserId()
        );

        return List.of(notifiedUser);
    }
}
