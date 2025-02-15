package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserServiceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserServiceDto getUser(@PathVariable long id);

    @PostMapping("/users")
    List<UserServiceDto> getUsers(List<Long> userIds);

    @PostMapping("/users/ordered")
    List<UserServiceDto> getOrderedUsers(List<Long> userIds);
}
