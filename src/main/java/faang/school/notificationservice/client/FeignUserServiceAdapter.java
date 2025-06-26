package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeignUserServiceAdapter {
    private final UserServiceClient userFeignClient;

    @Retryable(retryFor = {FeignException.class, SocketTimeoutException.class},
            maxAttemptsExpression = "${feign.retry.maxAttempts}",
            backoff = @Backoff(
                    delayExpression = "${feign.retry.delay}",
                    multiplierExpression = "${feign.retry.multiplier}"
            ))
    public Optional<UserDto> fetchUserDtosViaFeign(Long userId,
                                                   String entityNameForLog,
                                                   Long entityIdForLog) {
        if (userId == null) {
            log.info("Skipping user fetch via FeignClient for event '{}', " +
                            "ID {} as no user IDs were provided to fetch.",
                    entityNameForLog, entityIdForLog);
            return Optional.empty();
        }
        return Optional.of(userFeignClient.getUser(userId));
    }

    @Recover
    private Optional<UserDto> recoverFetchUserDtos(FeignException e,
                                                   Long userId,
                                                   String entityNameForLog,
                                                   Long entityIdForLog) {
        log.error("All retry attempts failed for FeignClient call (FeignException) for event '{}', ID {}. " +
                        "Requested User ID: {}. Status: {}, Response: '{}'. Error: {}",
                entityNameForLog,
                entityIdForLog,
                userId,
                e.status(),
                e.contentUTF8(),
                e.getMessage(),
                e);
        return Optional.empty();
    }

    @Recover
    private Optional<UserDto> recoverFetchUserDtos(SocketTimeoutException e,
                                                   Long userId,
                                                   String entityNameForLog,
                                                   Long entityIdForLog) {
        log.error("All retry attempts failed for FeignClient call (SocketTimeoutException) for event '{}', ID {}. " +
                        "Requested User ID: {}. Error: {}",
                entityNameForLog,
                entityIdForLog,
                userId,
                e.getMessage(),
                e);
        return Optional.empty();
    }
}
