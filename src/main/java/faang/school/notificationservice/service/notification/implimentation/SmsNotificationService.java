package faang.school.notificationservice.service.notification.implimentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.config.properties.ExolveProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.sms.SmsDto;
import faang.school.notificationservice.enums.PreferredContact;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
public class SmsNotificationService extends AbstractNotificationService{

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ExolveProperties exolveProperties;

    public SmsNotificationService(HttpClient httpClient, ObjectMapper objectMapper, ExolveProperties exolveProperties) {
        super(PreferredContact.PHONE);
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.exolveProperties = exolveProperties;
    }

    @Override
    public void send(UserDto user, String message) {
        String phone = user.getPhone();
        SmsDto smsDto = SmsDto.builder()
                .number(exolveProperties.getSms().getServiceNumber())
                .destination(phone)
                .text(message)
                .build();

        String body = convertDtoToString(smsDto);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(exolveProperties.getSms().getUri()))
                .header("Authorization", "Bearer " + exolveProperties.getApi().getKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        sendRequest(req);

    }

    private void sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            checkResponse(response);
            log.info(response.body());
        } catch (Exception e) {
            log.error("Error while sms sending");
        }
    }

    private void checkResponse(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        if (statusCode >= 400) {
            throw new RuntimeException("HTTP request failed with status code: " + statusCode +
                    "\nResponse body: " + response.body());
        }
    }

    private String convertDtoToString(SmsDto smsDto) {
        try {
            return objectMapper.writeValueAsString(smsDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}