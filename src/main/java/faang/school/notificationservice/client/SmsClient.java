package faang.school.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "sending-sms",
        url = "${sms.url}",
        configuration = FeignConfig.class)
public interface SmsClient {
    @GetMapping(value = "messages/v2/send")
    String sendingSms(
            @RequestParam("phone") String phone,
            @RequestParam("text") String msg
    );
}
