package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.GoalCompletedEvent;
import org.springframework.stereotype.Component;

@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {
    public GoalCompletedEventListener() {
        super(GoalCompletedEvent.class);
    }

    // если ввести абстрактный класс для эвентов, то можно избавиться от этого метода и
    // сделать абстрактный класс лаконичнее, да и в целом понятнее ведь <AbstractEvent> говрит сам за себя в отличии от <T>
    // не ввел потому что у остальных возьникнут проблемы
    @Override
    protected Long getUserId(GoalCompletedEvent event) {
        return event.getUserId();
    }
}
