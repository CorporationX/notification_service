package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsNotificationFailedException;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final RestTemplate restTemplate;

    @Value("${smsc.toNumber}")
    private String toNumber;

    @Value("${smsc.login}")
    private String login;

    @Value("${smsc.password}")
    private String password;

    @Value("${smsc.url}")
    private String smsBaseUrl;

    @Override
    public void send(UserDto user, String message) {
        log.info("Start sending message method");

        String url = UriComponentsBuilder.fromHttpUrl(smsBaseUrl)
                .queryParam("login", login)
                .queryParam("psw", password)
                .queryParam("phones", toNumber)
                .queryParam("mes", message)
                .queryParam("fmt", 3)
                .toUriString();

        log.info("Constructed URL for SMSC request: " + url);

        try {
            String response = restTemplate.getForObject(url, String.class);
            log.info("Received SMSC response: {}", response);

            if (response == null || !response.contains("\"id\"")) {
                log.error("SMSC error encountered with response: {}", response);
                throw new SmsNotificationFailedException("SMSC error: " + response);
            }
        } catch (HttpClientErrorException e) {
            log.error("Client Error: {}", e.getMessage());
            throw new HttpClientErrorException(e.getStatusCode());
        } catch (HttpServerErrorException e) {
            log.error("Server Error: {}", e.getMessage());
            throw new HttpServerErrorException(e.getStatusCode());
        } catch (ResourceAccessException e) {
            log.error("Connection Error: {}", e.getMessage());
            throw new ResourceAccessException(e.getMessage());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
