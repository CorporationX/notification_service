package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, представляющий событие "лайка" для поста.
 * <p>Основные поля:
 * <ul>
 *     <li>Идентификатор автора поста ({@link #postAuthorId})</li>
 *     <li>Идентификатор пользователя, который поставил лайк ({@link #likerId})</li>
 *     <li>Идентификатор поста, которому был поставлен лайк ({@link #postId})</li>
 * </ul>
 * </p>
 *
 * @author gulnaz21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikePostEvent {
    private long postAuthorId;
    private long likerId;
    private long postId;
}
