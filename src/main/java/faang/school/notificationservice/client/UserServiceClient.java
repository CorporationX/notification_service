package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.dto.UserProfileSettingsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/{userId}/profile-settings")
    UserProfileSettingsDto getProfileSettings(@PathVariable Long userId);

    @GetMapping("/api/v1/users/{id}/contacts")
    UserContactsDto getUserContacts(@PathVariable long id);
}

