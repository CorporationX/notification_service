package faang.school.notificationservice.listener.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.profile.ProfileViewEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> {

    public ProfileViewEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    Map<Class<?>, MessageBuilder<?>> messageBuilders,
                                    Map<UserDto.PreferredContact, NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEvent.class, profileView -> {
            UserDto profileViewedAuthor = userServiceClient.getUser(profileView.getProfileViewedId());
            Locale userPreferedLocale =
                    profileViewedAuthor.getLocale() != null ? profileViewedAuthor.getLocale() : Locale.ENGLISH;
            String text = getMessage(profileView, userPreferedLocale);
            sendNotification(profileViewedAuthor, text);
        });
    }
}
