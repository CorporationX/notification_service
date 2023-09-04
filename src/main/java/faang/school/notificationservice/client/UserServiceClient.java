package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.skill.SkillDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/users/notification/{id}")
    UserDto getUserDtoForNotification(@PathVariable long id);

    @PostMapping("/users/{userId}/telegram/{telegramId}")
    UserDto sendTelegramId(@PathVariable long userId, @PathVariable long telegramId);

    @GetMapping("/skill/{skillId}")
    SkillDto getSkillById(@PathVariable Long skillId);
}
