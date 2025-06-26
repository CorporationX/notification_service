package faang.school.notificationservice.service;

import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.dto.sms.SmsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    @Value(value = "${exolve.sms.uri}")
    private String API_URL;

    @Value(value = "${exolve.api.key}")
    private String API_KEY;

    @Value(value = "${exolve.sms.service-number}")
    private String SERVICE_NUMBER;

    private final HttpClient httpClient;

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

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
