package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.bot.TelegramBot;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.repository.TelegramContactRepository;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.telegram.TelegramContactService;
import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import java.util.List;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final TelegramService notificationService1;

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
            // тут потом сделать мессадж билдер
            String messageText = "you have been subscribed to by a user with id = %d".formatted(event.followerId());
            notificationService.send(userDto, messageText);
        } catch (IOException e) {
            log.error("Error parsing from json: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
