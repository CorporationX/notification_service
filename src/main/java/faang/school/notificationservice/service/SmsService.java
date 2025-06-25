package faang.school.notificationservice.service;

import faang.school.notificationservice.model.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
public class SmsService implements NotificationService {

    @Value(value = "${exolve.sms.uri}")
    private String API_URL;

    @Value(value = "${exolve.api.key}")
    private String API_KEY;

    @Value(value = "${exolve.sms.service-number}")
    private String SERVICE_NUMBER;

    @Override
    public void send(UserDto user, String message) {
        String phone = user.getPhone();

        String body = String.format(
                "{\"number\":\"%s\",\"destination\":\"%s\",\"text\":%s}",
                SERVICE_NUMBER, phone, message
        );

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.error("Error while sms sending");
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
