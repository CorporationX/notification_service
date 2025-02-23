package faang.school.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.config.sms.SmsConnectionParam;
import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.dto.sms.SmsSendRequestDto;
import faang.school.notificationservice.dto.sms.SmsSendResponseDto;
import faang.school.notificationservice.exception.SendNotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsNotificationService implements NotificationService {
    private final RestTemplate restTemplate;
    private final SmsConnectionParam smsConnectionParam;
    private final ObjectMapper objectMapper;

    @Override
    public void send(UserServiceDto user, String message) {
        log.info("Sending sms message");
        checkPhoneNumber(user.getPhone());
        String sourceNumber = smsConnectionParam.getSourcePhoneNumber();
        String destinationNumber = user.getPhone();
        SmsSendRequestDto requestDto = new SmsSendRequestDto(sourceNumber, destinationNumber, message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", smsConnectionParam.getAuthorizationString());
        HttpEntity<SmsSendRequestDto> request = new HttpEntity<>(requestDto, headers);
        String responseDto;

        try {
            responseDto = restTemplate.postForObject(smsConnectionParam.getUrl(), request,
                    String.class);
        } catch (HttpClientErrorException e) {
            throw new SendNotificationException("Send message by SMS failed - %s".formatted(e.getMessage()));
        }

        try {
            SmsSendResponseDto response = objectMapper.readValue(responseDto, SmsSendResponseDto.class);
            log.info("send message id {}", response.getMessageId());
        } catch (JsonProcessingException e) {
            throw new SendNotificationException("Send message by SMS failed, incorrect json format");
        }
    }

    @Override
    public UserServiceDto.PreferredContact getPreferredContact() {
        return UserServiceDto.PreferredContact.SMS;
    }

    private void checkPhoneNumber(String phoneNumber) {
        String regEx = "^7\\d{10}";
        if (!phoneNumber.matches(regEx)) {
            throw new SendNotificationException("Phone number is not valid");
        }
    }
}
