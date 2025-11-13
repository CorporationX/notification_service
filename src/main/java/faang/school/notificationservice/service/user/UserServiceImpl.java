package faang.school.notificationservice.service.user;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.exception.ExternalServiceException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final String USER_NOT_FOUND_MSG = "User %d not found";
    private final UserServiceClient userServiceClient;

    @Retryable(retryFor = {FeignException.InternalServerError.class, FeignException.ServiceUnavailable.class},
            maxAttemptsExpression = "${services.user-service.retryable.maxAttempts}",
            backoff = @Backoff(delayExpression = "${services.user-service.retryable.delay}",
                    multiplierExpression = "${services.user-service.retryable.multiplier}"))
    public UserDto getUserWithRetry(long userId) {
        return userServiceClient.getUser(userId);
    }

    @Override
    public UserDto getUser(long userId) {
        try {
            UserDto user = getUserWithRetry(userId);

            if (user == null || user.getId() == null || !user.getId().equals(userId)) {
                log.error(USER_NOT_FOUND_MSG.formatted(userId));
                throw new EntityNotFoundException(USER_NOT_FOUND_MSG.formatted(userId));
            }
            return user;

        } catch (FeignException.NotFound e) {
            log.error(USER_NOT_FOUND_MSG.formatted(userId));
            throw new EntityNotFoundException(USER_NOT_FOUND_MSG.formatted(userId));
        } catch (FeignException e) {
            log.error("Failed to get user {} after retries", userId, e);
            throw new ExternalServiceException("User service unavailable");
        }
    }
}