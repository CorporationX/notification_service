package faang.school.notificationservice.repository;

import faang.school.notificationservice.model.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationEventLogRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean checkExistingEvent(long id, NotificationType notificationType) {
        String sql = "SELECT EXISTS(SELECT 1 FROM notification_event_log WHERE event_id = ? and event_type = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id, notificationType.ordinal()));
    }

    public void saveNotificationEvent(long id, NotificationType notificationType) {
        String sql = "INSERT INTO notification_event_log (event_id, event_type) VALUES (?,?)";
        jdbcTemplate.update(sql, id, notificationType.ordinal());
    }
}
