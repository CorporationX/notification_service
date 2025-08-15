package faang.school.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "prostor-sms", url = "${sms.prostorsms.api.baseUrl}", configuration = ProstoSmsFeignConfig.class)
public interface ProstorSmsClient {
    @GetMapping("/send")
    String sendMessage(@RequestParam String phone, @RequestParam String text);

    @GetMapping("/status")
    String getMessageStatus(@RequestParam String id);
}
