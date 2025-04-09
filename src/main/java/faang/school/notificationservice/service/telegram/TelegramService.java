package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Сервис для отправки уведомлений через Telegram.
 * Реализует {@link NotificationService}, поддерживая повторные попытки при ошибках.
 *
 * <p>Использует {@link TelegramBot} для взаимодействия с Telegram API.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramBot telegramBot;

    /**
     * Отправляет сообщение пользователю через Telegram, если это его предпочтительный способ связи.
     *
     * @param user    получатель уведомления
     * @param message текст уведомления
     * @throws TelegramApiException если не удалось отправить сообщение после всех попыток
     */
    @Retryable(retryFor = TelegramApiException.class,
            maxAttemptsExpression = "${spring.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${spring.retry.backoff.delay}")
    )
    @Override
    public void send(UserDto user, String message) {
        log.info("Sending user {} to {}", user, message);
        if (user.getPreference() != getPreferredContact()) {
            log.debug("user preferences is not telegram");
            return;
        }

        try {
            telegramBot.sendMessage(user.getTelegramChatId(), message);
        } catch (TelegramApiException e) {
            log.error("Error while sending notification", e);
        }
        log.info("Sending user {} to {}", user, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    /**
     * Вызывается после исчерпания всех попыток отправки.
     *
     * @param e       исключение, вызвавшее сбой
     * @param user    получатель
     * @param ignored текст сообщения (не используется)
     */
    @Recover
    public void recoverSend(TelegramApiException e, UserDto user, String ignored) {
        log.error("Failed after all attems. ChatId: {}, Error {}",
                user.getTelegramChatId(), e.getMessage());
    }
}