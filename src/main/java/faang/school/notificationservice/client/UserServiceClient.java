package faang.school.notificationservice.client;

import faang.school.notificationservice.config.client.feign.FeignClientConfig;
import faang.school.notificationservice.dto.client.user_service.UserDto;
import faang.school.notificationservice.dto.client.user_service.EventDto;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service",
        url = "${services.user-service.host}:${services.user-service.port}",
        path = "/api/v1",
        configuration = FeignClientConfig.class)
public interface UserServiceClient {
    @Retryable(
            retryFor = { FeignException.class, RetryableException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @GetMapping("/users/{userId}/notification")
    UserDto getUserById(@PathVariable long userId);

    @Retryable(
            retryFor = { FeignException.class, RetryableException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @GetMapping("/users")
    List<UserDto> getUsersByIds(@RequestParam List<Long> userIds);
}