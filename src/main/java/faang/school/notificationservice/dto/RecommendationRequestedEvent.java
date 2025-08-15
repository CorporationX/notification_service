package faang.school.notificationservice.dto;

/**
 * Класс-событие для отправки сообщения о запросе на рекомендацию (приходит с {@code UserService})
 *
 * @author Linempy
 * @since 13.08.2025
 */
public record RecommendationRequestedEvent(
        Long requesterId,
        Long receiverId,
        Long requestId) {

}