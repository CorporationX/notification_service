package faang.school.notificationservice.messages.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.LikeEventDto;
import faang.school.notificationservice.messages.builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class LikeEventListener extends AbstractEventListener<LikeEventDto> implements MessageListener {

    public LikeEventListener(UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             List<MessageBuilder<LikeEventDto>> messageBuilders,
                             ObjectMapper objectMapper) {
        super(userServiceClient, notificationServices, messageBuilders, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEventDto.class, likeEvent -> {
            String text = getMessage(likeEvent, Locale.UK);
            sendNotification(likeEvent.getAuthorId(), text);
        });
    }
}


//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        try {
//            LikeEventDto likeEventDto = objectMapper.readValue(message.getBody(), LikeEventDto.class);
//            log.info("Получено  событие лайка поста с ID: {}, автору поста с ID: {}, от пользователя с ID: {}",
//                    likeEventDto.getPostId(), likeEventDto.getAuthorId(), likeEventDto.getUserId());
//
//            log.info("Запрос через FeignClient на получения пользователя с ID: {}", likeEventDto.getAuthorId());
//            UserDto userDto = userServiceClient.getUser(likeEventDto.getAuthorId());
//            log.info("Получили пользователя с ID: {} через FeignClient", likeEventDto.getAuthorId());
//
//            notificationServices.stream()
//                    .filter(service -> service.getPreferredContact() == userDto.getPreference())
//                    .findFirst()
//                    .ifPresent(service -> service.send(userDto, "Получен новый лайк поста"));
//
//            log.info("Пользователю с ID: {} отправлено уведомление о лайке поста с ID: {}",
//                    likeEventDto.getAuthorId(), likeEventDto.getPostId());
//
//        } catch (IOException e) {
//            log.error("Ошибка в обработке", e);
//            throw new RuntimeException(e);
//        }
//    }
