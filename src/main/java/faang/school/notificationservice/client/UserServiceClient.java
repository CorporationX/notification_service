package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.ContactPreferenceDto;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{id}")
    UserDto getUser(@PathVariable long id);

    @GetMapping("/api/v1/contacts/preferences/{userName}")
    ContactPreferenceDto getContactPreference(@PathVariable String userName);
}
