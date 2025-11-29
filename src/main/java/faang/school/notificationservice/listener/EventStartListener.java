package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.exception.JsonProcessingException;
import faang.school.notificationservice.service.MessageBuilderUtils;
import faang.school.notificationservice.service.NotificationService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class EventStartListener extends AbstractNotification implements MessageListener {
    private final ObjectMapper objectMapper;
    private final MessageBuilderUtils<EventStartDto> messageBuilderUtils;

    public EventStartListener(ObjectMapper objectMapper,
                              MessageBuilderUtils<EventStartDto> messageBuilderUtils,
                              UserServiceClient userServiceClient, List<NotificationService> notificationServices) {
        super(userServiceClient, notificationServices);
        this.objectMapper = objectMapper;
        this.messageBuilderUtils = messageBuilderUtils;
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        log.info("Processing the message from the {} topic", pattern);
        try {
            EventStartDto dto = objectMapper.readValue(message.getBody(), EventStartDto.class);
            Locale locale = (dto.locale() == null) ? Locale.ENGLISH : dto.locale();
            String text = messageBuilderUtils.getMessage(dto, locale);
            log.info("Attendees count {}", dto.attendeesIds().size());
            dto.attendeesIds().forEach(attendees -> sendNotification(attendees, text));
        } catch (IOException e) {
            log.error("Error processing start event");
            throw new JsonProcessingException("Error for listener Event Start %s" + e.getCause());
        }
    }
}