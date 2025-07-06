package faang.school.notificationservice.telegram.notification;

import faang.school.notificationservice.telegram.ActionExecutor;
import faang.school.notificationservice.telegram.NotificationAction;
import faang.school.notificationservice.telegram.NotificationActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActionExecutorImpl implements ActionExecutor {
    private final List<NotificationAction> actions;

    @Override
    public NotificationAction getAction(NotificationActionType notificationActionType) {
        return actions.stream()
                .filter((action) -> action.isAcceptable(notificationActionType))
                .findFirst()
                .orElseThrow();
    }
}
