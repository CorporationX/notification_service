package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserDto;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}/api/v1/users")
public interface UserServiceClient {

    @Retryable(retryFor = FeignException.class,
            maxAttemptsExpression = "${user-service.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${user-service.retry.backoff}"))
    @GetMapping("/{id}/locale/contact-preference")
    UserDto getUser(@PathVariable long id);

    @PostMapping("telegram/{telegramId}")
    UserDto registrationTelegramId(@PathVariable long telegramId);
}
