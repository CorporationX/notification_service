package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class SMSService implements NotificationService{
    //Выполняется в задаче BJS2-41826
    @Override
    public void send(UserDto user, String message) {
        System.out.println("Сообщение отправлено по смс " + message + " пользователю " + user.getUsername());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
