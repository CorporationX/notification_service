package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Log4j2
@RequiredArgsConstructor
public class UserGetter {
    public final static String USER_NOT_FOUND_BY_ID = "User with id = %s %s";
    public final static String INTERNAL_SERVER_ERROR = "Internal Server Error userId = %s, reason = %s";

    private final UserServiceClient userServiceClient;

    public UserDto getUserWithValidate(Long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (FeignException e) {
            if (e.status() < 0) {
                throwException(String.format(INTERNAL_SERVER_ERROR, userId, HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase()), (message) -> {
                    throw new RuntimeException(message);
                });
            }
            HttpStatus httpStatus = HttpStatus.valueOf(e.status());
            if (httpStatus.is4xxClientError()) {
                throwException(String.format(USER_NOT_FOUND_BY_ID, userId, httpStatus.getReasonPhrase()), (message) -> {
                    throw new EntityNotFoundException(message);
                });
            }
            if (httpStatus.is5xxServerError()) {
                throwException(String.format(INTERNAL_SERVER_ERROR, userId, httpStatus.getReasonPhrase()), (message) -> {
                    throw new RuntimeException(message);
                });
            }
        }
        String message = String.format(INTERNAL_SERVER_ERROR, userId, HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
        log.error(message);
        throw new RuntimeException(message);
    }

    private void throwException(String message, Consumer<String> stringConsumer) {
        log.error(message);
        stringConsumer.accept(message);
    }
}
