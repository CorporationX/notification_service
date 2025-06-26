package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.telegram.UserTelegramDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{id}")
    UserDto getUser(@PathVariable long id);

    @PostMapping("/api/v1/users")
    List<UserDto> getUsersByIds(@RequestBody List<Long> ids);

    @GetMapping("/api/v1/users/{userId}/telegram")
    UserTelegramDto getUserTelegram(@PathVariable long userId);

    @GetMapping("/api/v1/users/telegram/{telegramUserName}")
    UserTelegramDto getUserByTelegram(@PathVariable String telegramUserName);

    @PostMapping("/api/v1/users/telegram")
    UserTelegramDto addUserTelegram(@RequestBody UserTelegramDto userTelegramDto);
}
