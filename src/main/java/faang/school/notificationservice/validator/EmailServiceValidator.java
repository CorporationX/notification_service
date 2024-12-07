package faang.school.notificationservice.validator;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailServiceValidator {

    private final UserServiceClient userServiceClient;

    public void validateUserDto(UserDto userDto) {
        try {
            userServiceClient.getUser(userDto.getId());
        } catch (FeignException e) {
            throw new EntityNotFoundException("User with id %s not found".formatted(userDto.getId()));
        }
    }

    public void validateMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
    }
}
