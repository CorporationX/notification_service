package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFeignService {
    private final UserServiceClient userServiceClient;

    @Retryable(interceptor = "retryInterceptor")
    public UserContactsDto getUserContacts(Long userId) {
        try {
            return userServiceClient.getUserContacts(userId);
        } catch (FeignException e) {
            log.error("Error occurred while fetching user contacts for user {}", userId, e);
            throw e;
        }
    }
}
