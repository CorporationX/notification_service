package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.GoalDto;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(path = "/api", name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto getUser(@PathVariable long id);

    @GetMapping("/goals/{goalId}")
    GoalDto getGoalById(@PathVariable Long goalId);

    @GetMapping("/users/{userId}")
    boolean isUserExists(@PathVariable long userId);
}
