package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEventDto> {

    private final Locale DEFAULT_LOCALE = Locale.US;

    public ProfileViewEventListener(@Qualifier("objectMapperByRedis") ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<ProfileViewEventDto>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    @SneakyThrows
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("redis message: " + message.toString());
            ProfileViewEventDto event = objectMapper.readValue(message.getBody(), ProfileViewEventDto.class);
            String messageText = getMessage(event, DEFAULT_LOCALE);
            log.info("message text: " + messageText);
            sentNotification(event.getProfileId(), messageText);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}