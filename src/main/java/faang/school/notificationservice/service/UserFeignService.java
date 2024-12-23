package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFeignService {
    private final UserServiceClient userServiceClient;

    @Retryable(retryFor = Exception.class,
            maxAttemptsExpression = "@retryProperties.maxAttempts",
            backoff = @Backoff(
                    delayExpression = "@retryProperties.initialDelay",
                    multiplierExpression = "@retryProperties.multiplier",
                    maxDelayExpression = "@retryProperties.maxDelay"
            )
    )
    public UserContactsDto getUserContacts(Long userId) {
        try {
            return userServiceClient.getUserContacts(userId);
        } catch (FeignException e) {
            log.error("Error occurred while fetching user contacts for user {}", userId, e);
            throw e;
        }
    }
}