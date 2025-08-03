package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.telegram.UserTelegramDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.url}", path = "${user-service.path}")
public interface UserServiceClient {

    @GetMapping("/{id}")
    UserDto getUser(@PathVariable Long id);

    @PostMapping()
    List<UserDto> getUsersByIds(@RequestBody List<Long> ids);

    @GetMapping("/{userId}/telegram")
    UserTelegramDto getUserTelegram(@PathVariable long userId);

    @GetMapping("/telegram/{telegramUserName}")
    UserTelegramDto getUserByTelegram(@PathVariable String telegramUserName);

    @PostMapping("/telegram")
    UserTelegramDto addUserTelegram(@RequestBody UserTelegramDto userTelegramDto);
}
