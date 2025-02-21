package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserChatIdUpdateDto;
import faang.school.notificationservice.dto.UserNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}${user-service.servlet_path}")
public interface UserServiceClient {

    @GetMapping("/users/{id}/notification")
    UserNotificationDto getUserNotificationDto(@PathVariable long id);

    @PutMapping("/users/chat")
    UserNotificationDto updateUserChat(@RequestBody UserChatIdUpdateDto userChatIdUpdateDto);
}
