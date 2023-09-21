package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{userId}/internal")
    UserDto getUserInternal(@PathVariable long userId);

    @GetMapping("/api/v1/users/contact/")
    ContactDto getContactByContent(@RequestParam String content);
}
