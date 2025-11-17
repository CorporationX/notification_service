package faang.school.notificationservice.service;

import faang.school.notificationservice.config.provider.SmsRuProperties;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsNotificationService implements NotificationService {

    private final WebClient webClient;
    private final SmsRuProperties properties;

    @Override
    public void send(UserDto user, String message) {
        String phone = user.getPhone();

        if (!isUserPhoneValid(phone)) {
            log.warn("User {} has invalid or empty phone number: {}", user.getId(), phone);
            return;
        }

        try {
            webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(properties.getUrl())
                            .queryParam("api_id", properties.getKey())
                            .queryParam("to", phone)
                            .queryParam("msg", message)
                            .queryParam("json", 1)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to send SMS to {} via sms.ru", user.getPhone(), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

    private boolean isUserPhoneValid(String phone) {
        return phone != null && !phone.isBlank() && phone.matches("\\d{11}");
    }
}
