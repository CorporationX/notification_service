package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipRequestEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;


@Component
public class MentorshipRequestEventListener extends EventsListener<MentorshipRequestEvent> implements MessageListener {

    public MentorshipRequestEventListener(ObjectMapper objectMapper,
                                          UserServiceClient userClient,
                                          List<MessageBuilder<MentorshipRequestEvent>> messageBuilders,
                                          List<NotificationService> notifyServices) {
        super(objectMapper, userClient, messageBuilders, notifyServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipRequestEvent.class, event -> {
            UserDto mentor = userClient.getUser(event.getMentorId());
            Locale locale = LocaleContextHolder.getLocale();
            String textMessage = getMessage(event, locale);
            sendNotification(mentor, textMessage);
        });
    }
}
