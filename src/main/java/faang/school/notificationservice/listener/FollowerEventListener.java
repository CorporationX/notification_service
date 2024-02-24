package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Component;


@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> {
    private final UserServiceClient userServiceClient;

    public FollowerEventListener(UserServiceClient userServiceClient) {
        super(FollowerEventDto.class);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDto getUser(FollowerEventDto event) {
        return userServiceClient.getUser(event.getFolloweeId());
    }
}
