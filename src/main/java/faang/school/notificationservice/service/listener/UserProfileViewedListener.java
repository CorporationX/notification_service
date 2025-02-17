package faang.school.notificationservice.service.listener;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.dto.kafka.UserProfileViewedDto;
import faang.school.notificationservice.exception.handler.EventHandler;
import faang.school.notificationservice.exception.handler.KafkaMapperHandler;
import faang.school.notificationservice.exception.handler.MessageHandler;
import faang.school.notificationservice.exception.handler.NotificationServiceHandler;
import faang.school.notificationservice.exception.handler.UserServiceHandler;
import faang.school.notificationservice.exception.impl.non_retryable.NotFoundElementException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserProfileViewedListener extends AbstractEventListener<UserProfileViewedDto> {

    public UserProfileViewedListener(
            EventHandler eventHandler,
            UserServiceHandler userServiceHandler,
            NotificationServiceHandler notificationServiceHandler,
            MessageHandler<UserProfileViewedDto> messageHandler,
            KafkaMapperHandler kafkaMapperHandler) {
        super(eventHandler, userServiceHandler, notificationServiceHandler, messageHandler, kafkaMapperHandler);
    }

    @KafkaListener(topics = "${user-profile-viewed.topic-name}")
    public void listen(ConsumerRecord<String, Object> kafkaEvent) {
        UserProfileViewedDto inputDto = handleUniqueEvent(kafkaEvent, UserProfileViewedDto.class);
        List<UserServiceDto> users = getOrderedUsers(List.of(inputDto.profileOwnerId(), inputDto.viewerId()));

        Map<Long, UserServiceDto> userMap = getExistsUsersOrThrow(users, inputDto);
        UserServiceDto profileOwner = userMap.get(inputDto.profileOwnerId());
        UserServiceDto viewer = userMap.get(inputDto.viewerId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        String formattedDate = inputDto.viewedTime().format(formatter);

        List<String> additionalWordsForOwnerMessage = List.of(viewer.getUsername(), formattedDate);
        String message = getMessage(inputDto, profileOwner, additionalWordsForOwnerMessage);

        log.info("sending message {}", message);
        sendSingleNotification(profileOwner, message);
    }

    private Map<Long, UserServiceDto> getExistsUsersOrThrow(List<UserServiceDto> users, UserProfileViewedDto inputDto) {
        Map<Long, UserServiceDto> userMap = users.stream()
                .collect(Collectors.toMap(UserServiceDto::getId, Function.identity()));

        Long ownerId = inputDto.profileOwnerId();
        Long viewerId = inputDto.viewerId();

        if (!userMap.containsKey(ownerId) || !userMap.containsKey(viewerId)) {
            String error = String.format("User not found: profileOwnerId=%d, viewerId=%d", ownerId, viewerId);
            log.error(error);
            throw new NotFoundElementException(error);
        }

        return userMap;
    }
}
