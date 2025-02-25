package faang.school.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.config.sms.SmsConnectionParam;
import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.exception.SendNotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class SmsNotificationServiceTest {

    @Test
    void send() {
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();

        SmsConnectionParam param = new SmsConnectionParam();
        param.setUrl("https://api.exolve.ru/messaging/v1/SendSMS");
        param.setAuthorizationString("AUTH_KEY");
        param.setSourcePhoneNumber("79990004433");
        NotificationService smsNotificationService = new SmsNotificationService(restTemplate, param, objectMapper);

        UserServiceDto userDto = new UserServiceDto();
        userDto.setId(1L);
        userDto.setPhone("79990004433");

        String responseString = "{\n\t\"message_id\": \"123\"\n}";
        Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(responseString);

        Assertions.assertDoesNotThrow(() ->
                smsNotificationService.send(userDto, "Сообщение для абонента 1"));
    }

    @Test
    void sendFailed() {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        SmsConnectionParam param = new SmsConnectionParam();
        NotificationService smsNotificationService = new SmsNotificationService(restTemplate, param, objectMapper);

        UserServiceDto userDto = new UserServiceDto();
        userDto.setId(1L);
        userDto.setPhone("12345678901");

        Assertions.assertThrows(SendNotificationException.class, () ->
                smsNotificationService.send(userDto, "Сообщение для абонента %d".formatted(userDto.getId())));
    }

    @Test
    void sendWithIncorrectUrl() {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        SmsConnectionParam param = new SmsConnectionParam();
        param.setUrl("https://api.exolve.ru/messaging/v1/Send");
        param.setAuthorizationString("SMS_AUTH_KEY");
        param.setSourcePhoneNumber("79990004433");
        NotificationService smsNotificationService = new SmsNotificationService(restTemplate, param, objectMapper);

        UserServiceDto userDto = new UserServiceDto();
        userDto.setId(1L);
        userDto.setPhone("79990004433");

        Assertions.assertThrows(SendNotificationException.class, () ->
                smsNotificationService.send(userDto, "Сообщение для абонента %d".formatted(userDto.getId())));
    }
}