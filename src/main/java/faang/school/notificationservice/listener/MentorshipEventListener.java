package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.messageBuilder.MentorshipEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipEventListener implements MessageListener {
    private final UserServiceClient userServiceClient;
    private final JsonObjectMapper jsonObjectMapper;
    private final MentorshipEventMessageBuilder messageBuilder;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipEventDto event = jsonObjectMapper.readValue(message.getBody(), MentorshipEventDto.class);
        UserDto requester = userServiceClient.getUser(event.getRequesterId());
        Long eventRequesterId = event.getRequesterId();
        Long eventReceiverId = event.getReceiverId();
        sendEventNotifications(eventRequesterId, eventReceiverId);
    }

    private void sendEventNotifications(Long eventRequesterId, Long eventReceiverId) {
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
