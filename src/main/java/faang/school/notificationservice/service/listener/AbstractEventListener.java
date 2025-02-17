package faang.school.notificationservice.service.listener;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.exception.handler.EventHandler;
import faang.school.notificationservice.exception.handler.KafkaMapperHandler;
import faang.school.notificationservice.exception.handler.MessageHandler;
import faang.school.notificationservice.exception.handler.NotificationServiceHandler;
import faang.school.notificationservice.exception.handler.UserServiceHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final EventHandler eventHandler;
    private final UserServiceHandler userServiceHandler;
    private final NotificationServiceHandler notificationServiceHandler;
    private final MessageHandler<T> messageHandler;
    private final KafkaMapperHandler kafkaMapperHandler;

    protected T handleUniqueEvent(ConsumerRecord<String, Object> kafkaEvent, Class<T> clazz) {
        eventHandler.checkEventDuplicatedThrow(kafkaEvent);
        return kafkaMapperHandler.mapAndValidateKafkaEvent(kafkaEvent.value(), clazz);
    }

    protected UserServiceDto getSingleUser(Long userId) {
        return userServiceHandler.getSingleUser(userId);
    }

    protected List<UserServiceDto> getUserList(List<Long> userIds) {
        return userServiceHandler.getUserList(userIds);
    }

    protected List<UserServiceDto> getOrderedUsers(List<Long> userIds) {
        return userServiceHandler.getOrderedUsers(userIds);
    }

    protected String getMessage(T inputDto, UserServiceDto profileOwner, List<String> additionalWordsForOwnerMessage) {
        return messageHandler.getMessage(inputDto, profileOwner, additionalWordsForOwnerMessage);

    }

    protected void sendSingleNotification(UserServiceDto profileOwner, String message) {
        notificationServiceHandler.sendSingleNotification(profileOwner, message);
        eventHandler.saveEvent();
    }

    protected void sendListOfNotification(List<UserServiceDto> profileOwners, String message) {
        notificationServiceHandler.sendListOfNotification(profileOwners, message);
        eventHandler.saveEvent();
    }
}
