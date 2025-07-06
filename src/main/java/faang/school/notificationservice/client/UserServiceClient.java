package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.RegisterTelegramDto;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto getUser(@PathVariable long id);

    @PostMapping("/users/list")
    List<UserDto> getUsers(@RequestBody List<Long> usersId);

    @PostMapping("/contacts/telegram")
    void registerTelegramChatId(@RequestBody RegisterTelegramDto registerTelegram);

    @DeleteMapping("/contacts/telegram/{chatId}")
    void unregisterTelegramChatId(@PathVariable String chatId);
}
