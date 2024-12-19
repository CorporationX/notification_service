package faang.school.notificationservice.client;

import faang.school.notificationservice.model.ContactPreference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "localhost:8080")
public interface ContactPreferenceClient {

    @GetMapping("/{userId}")
    ContactPreference getContactPreferenceByUserId(@PathVariable("userId") long userId);
}
