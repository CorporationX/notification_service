package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {
    private final UserServiceClient userServiceClient;
    public GoalCompletedEventListener(UserServiceClient userServiceClient) {
        super(GoalCompletedEvent.class);
        this.userServiceClient = userServiceClient;
    }

    // если ввести абстрактный класс для эвентов, то можно избавиться от этого метода и
    // сделать абстрактный класс лаконичнее, да и в целом понятнее ведь <AbstractEvent> говрит сам за себя в отличии от <T>
    // не ввел потому что у остальных возьникнут проблемы
    @Override
    protected UserDto getUserDto(GoalCompletedEvent event) {
        return userServiceClient.getUser(event.getUserId());
    }

}
