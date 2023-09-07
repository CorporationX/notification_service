package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.dto.ProfileViewEventDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEventDto> {


    public ProfileViewEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<Class<?>>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        ProfileViewEventDto profileViewEvent = convertToJSON(message, ProfileViewEventDto.class);
        String message2 = getMessage(profileViewEvent.getClass(), Locale.ENGLISH);
        sendNotification(profileViewEvent.getIdVisited(), message2);
    }
}
