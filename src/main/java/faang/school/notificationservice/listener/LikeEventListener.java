package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEventDto likeEventDto = objectMapper.readValue(message.getBody(), LikeEventDto.class);
            log.info("Получено  событие лайка поста с ID: {}, автору поста с ID: {}, от пользователя с ID: {}",
                    likeEventDto.getPostId(),
                    likeEventDto.getAuthorId(),
                    likeEventDto.getUserId());
            UserDto userDto = userServiceClient.getUser(likeEventDto.getAuthorId());
            log.info("Получили пользователя с ID: {} через FeignClient", likeEventDto.getAuthorId());
            notificationServices.stream()
                    .filter(service -> service.getPreferredContact() == userDto.getPreference())
                    .findFirst()
                    .ifPresent(service -> service.send(userDto, "Получен новый лайк поста"));
            log.info("Пользователю с ID: {} отправлено уведомление о лайке поста с ID: {}",
                    likeEventDto.getAuthorId(),
                    likeEventDto.getPostId());
        } catch (IOException e) {
            log.error("Ошибка в обработке", e);
            throw new RuntimeException(e);
        }
    }
}