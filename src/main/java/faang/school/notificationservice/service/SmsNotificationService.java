package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.sms.SmsNotificationServiceHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsNotificationService implements NotificationService {
    private final SmsNotificationServiceHandler smsNotificationServiceHandler;

    @Override
    public void send(UserDto user, String message) {
        HttpClient client = smsNotificationServiceHandler.getHttpClient();
        HttpRequest request = smsNotificationServiceHandler.getHttpRequest(user, message);

        smsNotificationServiceHandler.retryableSend(client, request);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
