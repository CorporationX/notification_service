package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipEventDto;
import faang.school.notificationservice.service.MessageBuilderUtils;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class MentorshipOfferedListener extends AbstractNotification implements MessageListener {
    private final ObjectMapper objectMapper;
    private final MessageBuilderUtils<MentorshipEventDto> messageBuilder;

    public MentorshipOfferedListener(ObjectMapper objectMapper,
                                     MessageBuilderUtils<MentorshipEventDto> messageBuilderUtils,
                                     UserServiceClient userServiceClient,
                                     List<NotificationService> notificationServiceList) {
        super(userServiceClient, notificationServiceList);
        this.objectMapper = objectMapper;
        this.messageBuilder = messageBuilderUtils;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);

            MentorshipEventDto event = objectMapper.readValue(json, MentorshipEventDto.class);
            String notificationText = messageBuilder.getMessage(event, Locale.getDefault());

            sendNotification(event.mentorId(), notificationText);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process mentorship event", e);
        }
    }
}
