package faang.school.notificationservice.config.client;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
@Component
public interface UserFeignClient {

    @GetMapping("api/v1/users/{usersId}")
    UserDto getUserDto(@PathVariable long usersId);
}