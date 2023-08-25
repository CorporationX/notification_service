package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messageBuilder.EventStartEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventStartEventListener extends AbstractEventListener {

    private final UserServiceClient userServiceClient;
    private final JsonObjectMapper jsonObjectMapper;
    private final EventStartEventMessageBuilder messageBuilder;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventStartEventDto event = jsonObjectMapper.readValue(message.getBody(), EventStartEventDto.class);
        List<UserDto> userDtos = event.getUserIds().stream()
                .map(userServiceClient::getUser)
                .toList();

        for (UserDto user : userDtos) {
            Locale userLocale = user.getLocale();
            String eventName = event.getTitle();

            String messageToSend = messageBuilder.buildMessage(userLocale, eventName);
            for (NotificationService service : notificationServices) {
                if (user.getPreferredContact() == service.getPreferredContact()) {
                    service.send(user, messageToSend);
                }
            }
        }
    }
}
