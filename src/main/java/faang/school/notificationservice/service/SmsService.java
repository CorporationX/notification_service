package faang.school.notificationservice.service;

import faang.school.notificationservice.client.SmsClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final SmsClient smsClient;
    private final UserServiceClient userServiceClient;

    public String sendSms(String msg, long userId) {
        UserDto user = userServiceClient.getUser(userId);
        if (user.getPhone() == null) {
            throw new IllegalArgumentException("Phone is null");
        }
//        String phone = "%2B" + user.getPhone();
        String phone = URLEncoder.encode(user.getPhone(), StandardCharsets.UTF_8);
        String mess = URLEncoder.encode(msg, StandardCharsets.UTF_8);

        return smsClient.sendingSms(phone, mess);
    }
}
