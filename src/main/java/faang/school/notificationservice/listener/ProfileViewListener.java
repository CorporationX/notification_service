package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.dto.event.ProfileViewEventDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.dto.user.UserNameDto;
import faang.school.notificationservice.messageBuilder.ProfileViewMessageBuilder;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.EventStartService;
import faang.school.notificationservice.service.sms_sending.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileViewListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final ProfileViewMessageBuilder messageBuilder;
    private final UserServiceClient userServiceClient;
    private final EmailService emailService;
    private final SmsService smsService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        ProfileViewEventDto event;
        try {
            event = objectMapper.readValue(messageBody, ProfileViewEventDto.class);
            String text = messageBuilder.buildMessage(event, "eng");
            UserNameDto user = userServiceClient.getUserName(event.getProfileOwnerId());

            switch (user.getPreference()) {
                case EMAIL -> emailService.sendMail(user.getEmail(), "Profile View Notification", text);
                case PHONE -> smsService.send(text);
                case TELEGRAM -> System.out.println("TELEGRAM: " + text);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}