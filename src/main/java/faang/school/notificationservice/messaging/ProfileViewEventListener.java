package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.builders.ViewProfileMessageBuilder;
import faang.school.notificationservice.messaging.events.ProfileViewEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {

    private final List<NotificationService> notificationServices;
    private final ViewProfileMessageBuilder viewProfileMessageBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent profileViewEvent;
        try {
            profileViewEvent = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserDto currentUserDto = UserDto.builder().id(profileViewEvent.getIdVisited()).preference(UserDto.PreferredContact.EMAIL).build();
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(currentUserDto.preference()))
                .forEach(service -> service.send(currentUserDto,
                        viewProfileMessageBuilder.buildMessage(Locale.ENGLISH, profileViewEvent)));
    }
}
