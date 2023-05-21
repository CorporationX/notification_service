package faang.school.notificationservice.service.notification;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Override
    public void send(UserDto user, String message) {
        vonageClient.getSmsClient().submitMessage(new TextMessage(
                "Vonage APIs",
                user.getPhone(),
                message
        ));
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
