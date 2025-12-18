package faang.school.notificationservice.async;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.exception.UserServiceClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class AsyncUserService {
    private final UserServiceClient userServiceClient;

    @Async(value = "taskExecutor")
    public CompletableFuture<UserDto> getUserDtoAsync(long userId) {
        log.info("Start to take user with user-service, id {}", userId);
        try {
            return CompletableFuture.completedFuture(userServiceClient.getUser(userId));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Failed to get user DTO for Id " + userId);
        } catch (Exception e) {
            throw new UserServiceClientException("Not logged error " + e.getCause());
        }
    }
}