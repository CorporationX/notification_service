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
                                    MessageBuilder<ProfileViewEventDto> messageBuilder) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        ProfileViewEventDto profileViewEvent = fromJsonToObject(message, ProfileViewEventDto.class);
        String messageToSend = getMessage(profileViewEvent, Locale.ENGLISH);
        sendNotification(profileViewEvent.getIdVisited(), messageToSend);
    }
}
