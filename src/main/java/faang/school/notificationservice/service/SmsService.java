package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.smsaero.SmsAero;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {

    @Value("${company.name}")
    private String companyName;

    private final SmsAero client;


    @Override
    public void send(UserDto user, String message) {
        String phone = user.getPhone();
        try {
            client.SendSms(phone, message, companyName);
            log.info("The SMS notification was successfully sent to the number {} with the message {}",
                    phone, message);
        } catch (Exception e) {
            log.warn("An error occurred when sending an sms notification to {} with the message {}, error - {}",
                    phone, message, e.getMessage());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
