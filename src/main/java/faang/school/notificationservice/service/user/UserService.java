package faang.school.notificationservice.service.user;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserServiceClient userServiceClient;

    public UserDto getUser(Long userId){
        return userServiceClient.getUser(userId);
    }
}
