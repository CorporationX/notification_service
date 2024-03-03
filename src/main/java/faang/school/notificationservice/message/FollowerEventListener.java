package faang.school.notificationservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final TelegramService telegramService;
    private final MessageSource messageSource;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEvent event;
        try {
            log.info("из json хотим сделать event, {}", message);
            event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            log.info("из json делаем event, {}", message);
        } catch (IOException e) {
            log.error("не получилось из json делаем event, {}", message);
            throw new RuntimeException(e);
        }
        long userId = event.getFolloweeId();
        UserDto user = userServiceClient.getUser(userId);
        String text = user.getUsername() + " " + messageSource.getMessage(
                "follower.new", null, null);
        telegramService.send(user, text);
    }
}
