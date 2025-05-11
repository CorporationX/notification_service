package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEventDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AchievementEventListener<T> {
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notifications;


    public void onMessage2(AchievementEventDto event) {

    }

}
