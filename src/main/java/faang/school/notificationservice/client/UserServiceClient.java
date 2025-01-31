package faang.school.notificationservice.client;

import faang.school.notificationservice.config.resilience4j.Resilience4jProperties;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.UserForNotificationDto;
import feign.Headers;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/notification/{userId}")
    @Retry(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @CircuitBreaker(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    UserForNotificationDto getUserForNotificationById(@Positive @PathVariable Long userId);

    @PostMapping("/api/v1/contacts")
    @Retry(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @CircuitBreaker(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    ContactDto createContact(ContactDto contactDto);

    @GetMapping("/api/v1/users/phone/{phone}")
    @Retry(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @CircuitBreaker(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    UserForNotificationDto getUserByPhone(@PathVariable String phone);

    @GetMapping("/api/v1/contacts/{contact_number}")
    @Retry(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @CircuitBreaker(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    ContactDto getContactByNumber(@PathVariable("contact_number") String contactNumber);

    @DeleteMapping("/api/v1/contacts/{contact_number}")
    @Retry(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @CircuitBreaker(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    void deleteContactByNumber(@PathVariable("contact_number") String contactNumber);
}
