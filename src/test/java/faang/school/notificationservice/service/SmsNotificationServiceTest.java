package faang.school.notificationservice.service;

import faang.school.notificationservice.config.sms.SmsConnectionParam;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

class SmsNotificationServiceTest {

    @Test
    void send() {
        RestTemplate restTemplate = new RestTemplate();

        SmsConnectionParam param = new SmsConnectionParam();
        param.setUrl("https://api.exolve.ru/messaging/v1/SendSMS");
        param.setAuthorizationString(System.getenv("SMS_AUTH_KEY"));
        param.setSourcePhoneNumber(System.getenv("SMS_SOURCE_PHONE"));
        NotificationService smsNotificationService = new SmsNotificationService(restTemplate, param);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPhone(System.getenv("SMS_DESTINATION_PHONE"));

        Assertions.assertDoesNotThrow(() ->
                smsNotificationService.send(userDto, "Сообщение для абонента %d".formatted(userDto.getId())));
    }

    @Test
    void sendFailed() {
        RestTemplate restTemplate = new RestTemplate();

        SmsConnectionParam param = new SmsConnectionParam();
        param.setUrl("https://api.exolve.ru/messaging/v1/SendSMS");
        param.setAuthorizationString(System.getenv("SMS_AUTH_KEY"));
        param.setSourcePhoneNumber(System.getenv("SMS_SOURCE_PHONE"));
        NotificationService smsNotificationService = new SmsNotificationService(restTemplate, param);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPhone("12345678901");

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () ->
                smsNotificationService.send(userDto, "Сообщение для абонента %d".formatted(userDto.getId())));
    }
}