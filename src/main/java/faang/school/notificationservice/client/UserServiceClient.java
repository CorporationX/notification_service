package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @PutMapping("/api/v1/users/{userId}/telegram")
    void updateUserTelegramChatId(
            @PathVariable("userId") long userId,
            @RequestParam("chatId") Long chatId
    );

    @GetMapping("/api/v1/users/{id}/profile")
    UserProfileDto getUserProfile(@PathVariable long id);
}
