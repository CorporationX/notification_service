package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {

    private final ObjectMapper mapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final  List<MessageBuilder> messageBuilders;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEvent event = mapper.readValue(message.getBody(), LikeEvent.class);
//            UserDto likingUser = userServiceClient.getUser(event.getLikingUserId());
//            UserDto likedUser = userServiceClient.getUser(event.getLikedUserId());
            UserDto likingUser = new UserDto();
            likingUser.setPreference(UserDto.PreferredContact.TELEGRAM);
            likingUser.getTelegramChatId();
            UserDto likedUser = new UserDto();
            likedUser.setPreference(UserDto.PreferredContact.TELEGRAM);
            likedUser.getTelegramChatId();
            String text = messageBuilders.stream()
                    .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No message builder found for " + event.getClass().getName()))
                    .buildMessage(event, Locale.UK);
            notificationServices.stream()
                    .filter(service -> service.getPreferredContact() == (likedUser.getPreference()))
                    .findFirst()
                    .ifPresent(service -> service.send(likingUser, text));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
