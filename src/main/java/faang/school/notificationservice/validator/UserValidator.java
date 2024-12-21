package faang.school.notificationservice.validator;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    public final static String USER_NOT_FOUND_BY_ID = "User with id = %s %s";
    public final static String INTERNAL_SERVER_ERROR = "Internal Server Error %s %s";

    private final UserServiceClient userServiceClient;

    public UserDto getUserWithValidate(Long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (FeignException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.status());
            if (httpStatus.is4xxClientError()) {
                throw new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, userId, httpStatus.getReasonPhrase()));
            }
            if (httpStatus.is5xxServerError()) {
                throw new RuntimeException(String.format(INTERNAL_SERVER_ERROR, userId, httpStatus.getReasonPhrase()));
            }
        }
        throw new RuntimeException(String.format(INTERNAL_SERVER_ERROR, userId, HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase()));
    }
}
