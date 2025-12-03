package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @PostMapping("/api/v1/users/chats/{chatId}") //TODO: изменить параметры на @RequestBody
    UserDto updateChatIdByEmail(@PathVariable long chatId, @RequestParam("email") String email);

    @GetMapping("/api/v1/users/{userId}")
    UserDto getUserById(@PathVariable long userId);

    @PostMapping("/api/v1/users/ids")
    List<UserDto> getUsersByIds(@RequestBody List<Long> ids);
}
