package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MtsExolveException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsNotificationServiceHandler {
    private final SmsNotificationProperties properties;

    public HttpClient getHttpClient() {
        return HttpClient.newHttpClient();
    }

    public HttpRequest getHttpRequest(UserDto user, String message) {
        log.info("Notification message: " + message);

        String authHeader = properties.getAuthPrefix() + " " + properties.getApiKey();
        URI smsURI = URI.create(properties.getSmsURI());

        JSONObject body = new JSONObject()
                .put(properties.getSenderKey(), properties.getSenderNumber())
                .put(properties.getReceiverKey(), user.getPhone())
                .put(properties.getMessageKey(), message);

        return HttpRequest.newBuilder(smsURI)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .header(properties.getAuthKey(), authHeader)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Retryable(
            retryFor = {MtsExolveException.class},
            maxAttemptsExpression = "#{@smsNotificationProperties.getMaxAttempts()}",
            backoff = @Backoff(delayExpression = "#{@smsNotificationProperties.getDelay()}")
    )
    public void retryableSend(HttpClient client, HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response status code: {}; Response body: {}", response.statusCode(), response.body());
            statusCodeValidation(response);
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void statusCodeValidation(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            log.error(response.body());
            throw new MtsExolveException(response.body());
        }
    }
}