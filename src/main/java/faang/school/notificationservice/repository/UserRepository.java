package faang.school.notificationservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public void updateUserTelegramId(String login, long id) {
        String sql = "UPDATE users SET telegram_chat_id = ? WHERE telegram_login = ?";
        jdbcTemplate.update(sql, id, login);
    }
}
