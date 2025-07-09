package faang.school.notificationservice.listener.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.LikeEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class LikeEventListener extends AbstractEventListener<LikeEventDto> {

    public LikeEventListener(
        ObjectMapper objectMapper,
        UserServiceClient userServiceClient,
        List<NotificationService> notificationServices,
        List<MessageBuilder<LikeEventDto>> messageBuilders,
        Utils utils
    ) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, utils);
    }

    @KafkaListener(
        topics = "${spring.kafka.consumer.topics.likeTopic}",
        groupId = "#{'${spring.kafka.consumer.group-id}'}"
    )
    public void consume(String message) {
        LikeEventDto event = getEventDto(message, LikeEventDto.class);
        List<UserDto> users = userServiceClient.getUsers(List.of(event.getAuthorId(), event.getSenderId()));
        Optional<UserDto> authorOptional = getUser(users, event.getAuthorId());
        Optional<UserDto> senderOptional = getUser(users, event.getSenderId());
        if (authorOptional.isPresent() && senderOptional.isPresent()) {
            event.setSenderName(senderOptional.get().getUsername());
            UserDto author = authorOptional.get();
            String messageText = getMessage(event, new Locale(author.getLocale()));
            sendNotification(author, messageText);
        }
    }

    @Override
    void logError(Exception e) {
        log.error("like event error: {}", e.getMessage(), e);
    }

    private Optional<UserDto> getUser(List<UserDto> users, Long userId) {
        return users.stream()
            .filter(user -> user.getId() == userId)
            .findFirst();
    }
}
