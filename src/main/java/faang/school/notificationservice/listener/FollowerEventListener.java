package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.FollowerEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;
import java.util.List;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@Component
@Slf4j
public class FollowerEventListener extends AbstractEventListener {
    private final static Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private final FollowerEventMessageBuilder messageBuilder;

    public FollowerEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 FollowerEventMessageBuilder messageBuilder
                                 ) {
        super(objectMapper, userServiceClient, notificationServices);
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.toString(), FollowerEvent.class);
            UserDto userDto = userServiceClient.getUser(event.followeeId());
            NotificationService notificationService = null;
            for (NotificationService service : notificationServices) {
                if (Objects.equals(service.getPreferredContact(), userDto.getPreference())) {
                    notificationService = service;
                    break;
                }
            }
            if (notificationService == null) {
                throw new RuntimeException("Preferred notification method was not found");
            }
            String messageText = messageBuilder.buildMessage(event, DEFAULT_LOCALE);
            notificationService.send(userDto, messageText);
        } catch (IOException e) {
            log.error("Error parsing from json: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
