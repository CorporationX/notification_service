package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SendSmsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smsaero.SmsAero;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final SmsAero smsAeroClient;

    @Override
    public void send(UserDto user, String text) {
        try {
            smsAeroClient.SendSms(user.getPhone(), text);
        } catch (Exception e) {
            log.error("Error sending notification", e);
            throw new SendSmsException(e.getMessage());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
