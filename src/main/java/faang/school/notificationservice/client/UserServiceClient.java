package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.UserForNotificationDto;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/notification/{userId}")
    @Retry(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @CircuitBreaker(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    UserForNotificationDto getUserForNotificationById(@Positive @PathVariable long userId);

    @PostMapping("/api/v1/contacts")
    @Headers("x-user-id: 1")
    ContactDto createContact(ContactDto contactDto);

    @GetMapping("/api/v1/users/phone/{phone}")
    @Headers("x-user-id: 1")
    UserForNotificationDto getUserByPhone(@PathVariable String phone);

    @GetMapping("/api/v1/contacts/{contact_number}")
    @Headers("x-user-id: 1")
    ContactDto getContactByNumber(@PathVariable("contact_number") String contactNumber);

    @DeleteMapping("/api/v1/contacts/{contact_number}")
    @Headers("x-user-id: 1")
    void deleteContactByNumber(@PathVariable("contact_number") String contactNumber);
}
