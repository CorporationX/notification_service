
package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.exception.ExternalServiceException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserServiceClient userServiceClient;

    public UserDto getUserById(long id) {
        try {
            return userServiceClient.getUser(id);
        } catch (FeignException.BadRequest e) {
            throw new EntityNotFoundException("Пользователь не найден");
        } catch (FeignException e) {
            throw new ExternalServiceException("User service недоступен");
        }
    }
}