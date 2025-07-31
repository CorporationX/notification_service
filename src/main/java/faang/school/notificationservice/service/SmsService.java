package faang.school.notificationservice.service;

import faang.school.notificationservice.client.SmsClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {
    private final SmsClient smsClient;

    @Override
    public void send(UserDto user, String message) {
        if (user.getPhone() == null || user.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone is null");
        }
        String phone = "+" + user.getPhone();

        String result = smsClient.sendingSms(phone, message);

        log.info(result);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
