package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}/user")
public interface UserServiceClient {

    @GetMapping("/notify/{id}")
    UserNotificationDto getDtoForNotification(@PathVariable("id") long userId);
}
