package faang.school.notificationservice.redis.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClientMock;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.dto.ProfileViewEventDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEventDto> {
    private final ObjectMapper objectMapper;

    public ProfileViewEventListener(UserServiceClientMock userServiceClient,
                                    List<MessageBuilder<?>> messageBuilders,
                                    List<NotificationService> notificationServices,
                                    ObjectMapper javaTimeModuleObjectMapper) {
        super(userServiceClient, messageBuilders, notificationServices, javaTimeModuleObjectMapper, ProfileViewEventDto.class);
        this.objectMapper = javaTimeModuleObjectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            List<ProfileViewEventDto> profileViewEventDtoList = objectMapper.readValue(message.getBody(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProfileViewEventDto.class));

            profileViewEventDtoList.forEach(this::processEvent);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert json to events: {}, error:", message, e);
        } catch (Exception exception) {
            log.error("Failed to process events from Redis", exception);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEvent(ProfileViewEventDto event) {
        UserDto userDto = getUserDto(event.getActorId());

        MessageBuilder<ProfileViewEventDto> messageBuilder =
                (MessageBuilder<ProfileViewEventDto>) defineBuilder(ProfileViewEventDto.class);
        String message = messageBuilder.buildMessage(event, userDto.getLocale());

        sendNotification(userDto, message);
    }
}
