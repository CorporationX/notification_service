package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto getUserById(Long userId) {
        UserDto user = new UserDto();
        user.setId(userId);
        user.setPreference(UserDto.PreferredContact.EMAIL);
        user.setEmail("user" + userId + "@example.com");
        return user;
    }
}
