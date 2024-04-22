package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component

public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {

    public ProfileViewEventListener(ObjectMapper mapper,
                                    UserServiceClient userServiceClient,
                                    List<MessageBuilder<ProfileViewEvent>> messageBuilders, //why are injecting the list of precise eventType, if we have only one ProfileViewEventType messageBuilder
                                    JavaMailSender javaMailSender,
                                    List<NotificationService> notificationServices) {
        super(mapper, userServiceClient, messageBuilders, javaMailSender, notificationServices);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {

        handleEvent(message, ProfileViewEvent.class, (event) -> {

            ProfileViewEvent profileViewEvent = (ProfileViewEvent) event;
            UserDto observer = userServiceClient.getNotificationUser(profileViewEvent.getObserverId());
            String text = getMessage(profileViewEvent, Locale.UK);
            sendNotification(profileViewEvent.getObservedId(), text);
        });
    }
}
