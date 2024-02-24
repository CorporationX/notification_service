package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private VonageClient client;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return null;
    }

    @Override
    public void send(UserDto user, String message) {

    }

}
