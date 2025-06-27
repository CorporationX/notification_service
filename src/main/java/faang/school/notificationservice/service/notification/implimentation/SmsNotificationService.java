package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.sms.SmsDto;
import faang.school.notificationservice.enums.PreferredContact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
public class SmsNotificationService extends AbstractNotificationService{

    @Value(value = "${exolve.sms.uri}")
    private String API_URL;

    @Value(value = "${exolve.api.key}")
    private String API_KEY;

    @Value(value = "${exolve.sms.service-number}")
    private String SERVICE_NUMBER;

    private final HttpClient httpClient;

    public SmsNotificationService(HttpClient httpClient) {
        super(PreferredContact.PHONE);
        this.httpClient = httpClient;
    }

    @Override
    public void send(UserDto user, String message) {
        String phone = user.getPhone();

        String body = SmsDto.builder()
                .number(SERVICE_NUMBER)
                .destination(phone)
                .text(message)
                .build().toString();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            log.info(response.body());
        } catch (Exception e) {
            log.error("Error while sms sending");
        }
    }
}