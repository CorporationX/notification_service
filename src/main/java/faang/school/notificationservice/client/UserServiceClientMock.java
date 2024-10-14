package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.EMAIL;

@Component
public class UserServiceClientMock implements UserServiceClient {
    public UserDto getUser(long id) {
        return new UserDto(1, "john_doe", "john.doe@gmail.com", "+123-555-555", EMAIL, Locale.US);
    }
}
