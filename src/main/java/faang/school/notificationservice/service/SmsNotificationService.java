package faang.school.notificationservice.service;

import faang.school.notificationservice.config.provider.SmsRuProperties;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsNotificationService implements NotificationService {

    private final RestTemplate restTemplate;
    private final SmsRuProperties properties;

    @Override
    public void send(UserDto user, String message) {
        String phone = user.getPhone();
        if (phone == null || phone.isBlank()) {
            log.warn("User {} has no phone number. SMS not sent.", user.getId());
            return;
        }

        if (!phone.matches("\\d{11}")) {
            log.warn("Invalid phone format: {}", phone);
            return;
        }

        String url = UriComponentsBuilder.fromHttpUrl(properties.url())
                .queryParam("api_id", properties.key())
                .queryParam("to", phone)
                .queryParam("msg", message)
                .queryParam("json", 1)
                .toUriString();

        try {
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            log.error("Failed to send SMS to {} via sms.ru", user.getPhone(), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
