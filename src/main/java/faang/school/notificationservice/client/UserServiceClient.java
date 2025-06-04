package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${services.user-service.name}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto getUser(@PathVariable long id);
}
