package faang.school.notificationservice.client;

import faang.school.notificationservice.model.dto.SkillDto;
import faang.school.notificationservice.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto getUser(@PathVariable long id);

    @GetMapping("/skills/{userId}")
    List<SkillDto> getUserSkills(@PathVariable Long userId);
}
