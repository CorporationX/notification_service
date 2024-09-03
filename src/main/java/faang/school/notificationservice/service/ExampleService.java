package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import jakarta.validation.constraints.NotBlank;

public class ExampleService implements NotificationService {

    @Override
    public void send(UserDto userDto, @NotBlank String message) {
        System.out.println("Sending message: " + message + " to user: " + userDto);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
