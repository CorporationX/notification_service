package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.data.PreferredContact;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.SmsService;
import faang.school.notificationservice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {
    private final EmailService emailService;
    private final TelegramService telegramService;
    private final SmsService smsService;
    private final LikeMessageBuilder likeMessageBuilder;
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);

            json = json.replaceAll("\"@class\".*?,", "");

            LikeEvent event = objectMapper.readValue(json, LikeEvent.class);

            UserDto user = userServiceClient.getUser(event.getPostAuthorId());
            String preference = userServiceClient.getProfileSettings(event.getPostAuthorId()).getPreference();
            user.setPreference(PreferredContact.valueOf(preference));

            switch (preference) {
                case "TELEGRAM" -> telegramService.send(user, likeMessageBuilder.buildMessage(event, Locale.US));
                case "SMS" -> smsService.send(user, likeMessageBuilder.buildMessage(event, Locale.US));
                default -> emailService.send(user, likeMessageBuilder.buildMessage(event, Locale.US));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

