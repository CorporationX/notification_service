package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.ExtendedContactDto;
import faang.school.notificationservice.dto.TgContactDto;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto getUser(@PathVariable long id);

    @PostMapping("/{userId}/contacts")
    void updateUserContact(TgContactDto tgContactDto);

    @GetMapping("/{userId}/contacts")
    ExtendedContactDto getUserContact(@PathVariable Long userId);
}
