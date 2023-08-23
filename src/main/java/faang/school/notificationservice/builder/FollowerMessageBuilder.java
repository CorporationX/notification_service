package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FollowerMessageBuilder {

    private final UserServiceClient userServiceClient;

    @Value("${message.properties.follower.new}")
    private String followerMessage;

    public String getText(FollowerEvent event) {
        return followerMessage + " By the name of " + userServiceClient.getUser(event.getFollowerId()).getUsername() + ".";
    }
}
