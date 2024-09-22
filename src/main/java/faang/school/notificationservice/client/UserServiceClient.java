package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.EventDto;
import faang.school.notificationservice.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserDto getUser(@PathVariable long id);

    @GetMapping("/api/users")
    List<UserDto> getUsers(List<Long> userIds);

    @GetMapping("/api/v1/events/{id}")
    EventDto getEvent(@PathVariable long id);
}
