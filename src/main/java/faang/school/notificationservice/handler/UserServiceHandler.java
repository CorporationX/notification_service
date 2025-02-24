package faang.school.notificationservice.handler;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.exception.impl.retryable.UserServiceClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserServiceHandler {
    private final UserServiceClient userServiceClient;

    public UserServiceDto getSingleUser(Long userId) {
        return fetchUserWithHandling(() -> userServiceClient.getUser(userId));
    }

    public List<UserServiceDto> getUserList(List<Long> userIds) {
        return fetchUserWithHandling(() -> userServiceClient.getUsers(userIds));
    }

    public List<UserServiceDto> getUsersByIdsInGivenOrder(List<Long> userIds) {
        return fetchUserWithHandling(() -> userServiceClient.getUsersByIdsInGivenOrder(userIds));
    }

    private <R> R fetchUserWithHandling(Supplier<R> fetcher) {
        try {
            return fetcher.get();
        } catch (RuntimeException e) {
            String error = "Fail to get user";
            log.error(error, e);
            throw new UserServiceClientException(error);
        }
    }
}
