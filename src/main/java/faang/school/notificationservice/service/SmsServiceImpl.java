package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.SendSmsRequestDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error.SmsSendException;
import faang.school.notificationservice.mapper.NotificationMapper;
import faang.school.notificationservice.service.sms.SmsGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;
    private final SmsGateway smsGateway;

    @Override
    @Retryable(backoff = @Backoff(delay = 500, multiplier = 2.0))
    public void send(UserDto user, String message) {
        if (user.phone() == null || user.phone() .isBlank()) {
            throw new SmsSendException("User phone is empty");
        }
        smsGateway.send(user.phone() , message);
    }

    public void sendFromRequest(SendSmsRequestDto request, String message) {
        UserDto user = notificationMapper.toUserDto(request);
        send(user, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

}
