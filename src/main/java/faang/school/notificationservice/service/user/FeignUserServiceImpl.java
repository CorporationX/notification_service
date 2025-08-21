package faang.school.notificationservice.service.user;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.exception.ServiceUnavailableException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeignUserServiceImpl implements FeignUserService {

    private final UserServiceClient userServiceClient;

    @Override
    @Retryable(
            retryFor = ServiceUnavailableException.class,
            maxAttemptsExpression = "#{${user-service.retry.max-attempts}}",
            backoff = @Backoff(
                    delayExpression = "#{${user-service.retry.delay}}",
                    multiplierExpression = "#{${user-service.retry.multiplier}}",
                    maxDelayExpression = "#{${user-service.retry.max-delay}}"
            )
    )
    public UserDto getById(Long userId) {
        try {
            UserDto dto = userServiceClient.getUser(userId);
            if (dto == null) {
                log.info("User not found id={}", userId);
                throw new EntityNotFoundException("User not found with id: " + userId);
            }
            return dto;

        } catch (feign.RetryableException exception) {
            log.warn("User service temporary failure, will retry. id={}, msg={}", userId, exception.getMessage());
            throw new ServiceUnavailableException("User service unavailable");

        } catch (FeignException.NotFound exception) {
            log.info("User not found id={}", userId);
            throw new EntityNotFoundException("User not found with id: " + userId);

        } catch (FeignException exception) {
            log.warn("User service call failed id={}, message={}", userId, exception.getMessage());
            throw exception;
        }
    }
}
