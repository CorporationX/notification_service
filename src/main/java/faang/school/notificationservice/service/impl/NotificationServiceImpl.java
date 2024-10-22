package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void send(UserDto user, String message) {
        // тут должна быть логика с SmsService, TelegramService, EmailService, но ее пока не будет
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return null;
    }
}
