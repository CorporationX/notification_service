package faang.school.notificationservice.client;

import faang.school.notificationservice.model.dto.GoalDto;
import faang.school.notificationservice.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{id}")
    UserDto getUser(@PathVariable long id);

    @GetMapping("/api/v1/goals/{goalId}")
    GoalDto getGoal(@PathVariable long goalId);
}
