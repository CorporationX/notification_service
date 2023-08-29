package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class EventStartEventListener extends AbstractEventListener<EventStartEventDto> implements MessageListener {

    public EventStartEventListener(JsonObjectMapper jsonObjectMapper,
                                   UserServiceClient userServiceClient,
                                   List<MessageBuilder<EventStartEventDto>> followEventMessageBuilder,
                                   List<NotificationService> notificationServiceList) {
        super(jsonObjectMapper, userServiceClient, followEventMessageBuilder, notificationServiceList);
    }

        @Override
    public void onMessage(Message message, byte[] pattern) {
        EventStartEventDto event = jsonObjectMapper.readValue(message.getBody(), EventStartEventDto.class);
        String eventTitle = event.getTitle();
        List<UserDto> userDtos = fetchUserDtosByIds(event.getUserIds());
        sendEventNotifications(userDtos, eventTitle);
    }

    private void sendEventNotifications(List<UserDto> userDtos, String eventName) {
        for (UserDto user : userDtos) {
            Locale userLocale = user.getLocale();

            String messageToSend = messageBuilder.buildMessage(userLocale, eventName);
            for (NotificationService service : notificationServices) {
                if (user.getPreference() == service.getPreferredContact()) {
                    service.send(user, messageToSend);
                }
            }
        }
    }

    private List<UserDto> fetchUserDtosByIds(List<Long> userIds) {
        return userIds.stream()
                .map(userServiceClient::getUser)
                .toList();
    }
}
