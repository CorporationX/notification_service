package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Сервис для отправки SMS-сообщений
 *
 * @author Linempy
 * @since 12.08.2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final WebClient webClient;

    @Value("${sms.prostor.login}")
    private String login;

    @Value("${sms.prostor.password}")
    private String password;

    @Value("${sms.prostor.sender}")
    private String sender;

    @Value("${sms.prostor.api}")
    private String api;

    @Override
    public void send(UserDto user, String message) {
        log.info("Отправляется SMS к: {}, Сообщение: {}",
                user.getUsername(), message);

        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(api)
                        .queryParam("login", login)
                        .queryParam("password", password)
                        .queryParam("phone", user.getPhone())
                        .queryParam("text", message)
                        .queryParam("sender", sender)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("SMS отправлено успешно. Ответ: {}", response))
                .doOnError(error -> log.error("Ошибка в отправке SMS", error))
                .subscribe();
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}