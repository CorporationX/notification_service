package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.ProfileViewEventDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.ProfileViewMessageBuilder;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.sms_sending.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfileViewListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final ProfileViewMessageBuilder messageBuilder;
    private final UserServiceClient userServiceClient;
    private final EmailService emailService;
    private final SmsService smsService;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        ProfileViewEventDto event;
        try {
            event = objectMapper.readValue(messageBody, ProfileViewEventDto.class);
            String text = messageBuilder.buildMessage(event, "eng");
            UserDto user = userServiceClient.getUserNoPublish(event.getProfileOwnerId());

            notificationServices.stream().filter(service -> service.getPreferredContact() == user.getPreference())
                    .findFirst()
                    .ifPresent(service -> service.send(user, text));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}