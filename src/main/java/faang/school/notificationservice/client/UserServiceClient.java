package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}/api/v1")
public interface UserServiceClient {

    @GetMapping("users/{id}")
    UserDto getUser(@PathVariable long id);

    @PostMapping("users/get-by-ids")
    List<UserDto> getUsersByIds(@RequestBody List<Long> ids);

    @GetMapping("/events/{id}")
    EventDto getEvent(@PathVariable long id);
}
