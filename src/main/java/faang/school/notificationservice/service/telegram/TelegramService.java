package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ServiceCallException;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramService implements NotificationService {

    private final NotificationBot notificationBot;

    @Override
    @Retryable(
            retryFor = ServiceCallException.class,
            maxAttemptsExpression = "${telegram.notification-bot.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${telegram.notification-bot.retry.backoff-delay}", multiplier = 2)
    )
    public void send(UserDto user, String message) {
        notificationBot.sendMessage(user.getTelegramChatId(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    @Recover
    public void recover(ServiceCallException e, UserDto user, String message) {
        log.error("Retries exhausted. Could not send message to user {}: {}", user.getId(), e.getMessage());
    }
}
