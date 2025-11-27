package faang.school.notificationservice.config.context;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private final static Integer ADMIN_ID = 13;

    private final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public void setUserId(long userId) {
        userIdHolder.set(userId);
    }

    public long getUserId() {
        return userIdHolder.get() != null ? userIdHolder.get() : ADMIN_ID;

    }

    public void clear() {
        userIdHolder.remove();
    }
}
