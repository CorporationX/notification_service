package faang.school.notificationservice.service;

import faang.school.notificationservice.client.SmsClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService{
    private final SmsClient smsClient;

    @Override
    public void send(UserDto user, String message) {
        if (user.getPhone() == null) {
            throw new IllegalArgumentException("Phone is null");
        }
        String phone = "+" + user.getPhone();
//        String phone = "+79994677363";

        smsClient.sendingSms(phone, message);//Тут вроде как должен ответ быть
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
