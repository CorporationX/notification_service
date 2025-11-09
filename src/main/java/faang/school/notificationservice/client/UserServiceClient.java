package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://${services.user-service.host}:${services.user-service.port}" +
        "${services.user-service.servlet.context-path}/users")
public interface UserServiceClient {

    @GetMapping("/{id}")
    UserDto getUser(@PathVariable long id);
}