package faang.school.notificationservice.service.comment;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommentSmsNotificationService implements NotificationService {
    @Override
    public void send(UserDto user, String message) {
        UserDto.PreferredContact userPreferredContact = user.getPreference();
        log.info("Notification from: " + this.getClass().getSimpleName() + " by user preference: " + userPreferredContact);
        log.info("Notification message: " + message);

        //TODO Murzin34 Задействовать отправку нотификации по SMS
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
