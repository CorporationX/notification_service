package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}", path = "api/v1/users")
public interface UserServiceClient {
    @GetMapping("/{id}/utility")
    UserDto getUserUtility(@PathVariable long id);
}
