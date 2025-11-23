package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipEventDto;
import faang.school.notificationservice.exception.ValidationException;
import faang.school.notificationservice.service.MessageBuilderUtils;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.NoSuchMessageException;
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
        MentorshipEventDto event = null;

        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);

            event = objectMapper.readValue(json, MentorshipEventDto.class);
            String notificationText = messageBuilder.getMessage(event, Locale.getDefault());

            sendNotification(event.mentorId(), notificationText);
        } catch (ValidationException e) {
            if (event != null) {
                log.warn("Validation failed for user {}: {}", event.mentorId(), e.getErrors());
            } else {
                log.warn("Validation failed: {}", e.getErrors());
            }
        } catch (NoSuchMessageException e) {
            log.warn("Missing message key for mentorship event: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process mentorship event", e);
        }
    }
}
