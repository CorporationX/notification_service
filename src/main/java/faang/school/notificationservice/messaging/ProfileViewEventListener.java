package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.builders.ViewProfileMessageBuilder;
import faang.school.notificationservice.messaging.events.ProfileViewEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
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
    private final UserServiceClient userServiceClient;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent profileViewEvent = getEvent(message);
        UserDto currentUserDto =
                userServiceClient.getUserInternal(profileViewEvent.getIdVisited(), profileViewEvent.getIdVisitor());
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(currentUserDto.preference()))
                .forEach(service -> service.send(currentUserDto,
                        viewProfileMessageBuilder.buildMessage(Locale.ENGLISH, profileViewEvent)));
    }

    private ProfileViewEvent getEvent(Message message){
        ProfileViewEvent profileViewEvent;
        try {
            profileViewEvent = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return profileViewEvent;
    }

    private UserDto.PreferredContact getPreferredContact(int number){
        switch (number){
            case 1: return UserDto.PreferredContact.EMAIL;
            case 2: return UserDto.PreferredContact.SMS;
            case 3: return UserDto.PreferredContact.TELEGRAM;
            default: throw new IllegalArgumentException("Preferred contact data is invalid");
        }
    }
}
