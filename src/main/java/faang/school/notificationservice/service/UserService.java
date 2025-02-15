package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    public UserDto getUser(long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (FeignException.FeignClientException ex) {
            throw new EntityNotFoundException(String.format("Пользователь с id=%d не найден", userId));
        }
    }

    public boolean isUserExists(long userId) {
        try {
            userServiceClient.getUser(userId);
            return true;
        } catch (FeignException.FeignClientException ex) {
            return false;
        }
    }

    public void checkUser(long userId) {
        if (userContext.getUserId() != userId) {
            throw new DataValidationException("Id пользователя не совпадает с Id владельца");
        }
    }
}
