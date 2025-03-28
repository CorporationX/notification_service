package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

        @Override
        public UserDto.PreferredContact getPreferredContact() {
            return UserDto.PreferredContact.TELEGRAM;
        }

        @Override
        public void send(UserDto user, String message) {
        }

}
