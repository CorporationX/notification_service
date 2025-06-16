package faang.school.notificationservice.service;

import faang.school.notificationservice.client.SmsClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        String phone = "+" + user.getPhone();

        return smsClient.sendingSms(phone, msg);
    }
}
