package faang.school.notificationservice.client;

import faang.school.notificationservice.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto getUser(@PathVariable long id);

    @PutMapping("/users/updateTelegramUserId")
    UserDto updateTelegramUserId(@RequestParam String telegramUserName,
                                 @RequestParam String telegramUserId);
}
