package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final SmsService smsService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            var follower = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            log.info("Received message: {}", follower);
            emailService.sendEmail("any@gmail.com", "New follower", "You have a new follower! " + follower.toString());
            smsService.sendSms("1235463262", "Hello from Vonage SMS API");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
