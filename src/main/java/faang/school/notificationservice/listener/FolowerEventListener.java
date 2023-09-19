package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.messageBulder.MessageBuilder;
//import faang.school.notificationservice.service.EmailServiceFolower;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service

//public class FolowerEventListener extends AbstractEventListener<FollowerEvent> {
public class FolowerEventListener implements MessageListener {


//    public FolowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<MessageBuilder<FollowerEvent>> messageBuilders, List<NotificationService> notificationService) {
//        super(objectMapper, userServiceClient, messageBuilders, notificationService);
//    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("Hello world!");
            //FollowerEvent event = convertToJSON(message, FollowerEvent.class);
            //Locale locale = Locale.ENGLISH;
            // System.out.println(followee.getUsername()+"\n"+follower.getUsername());
           // String json = getMessage(event, locale);
           // sendNotification(event.getFolloweeId(),json);
            //System.out.println(json);
            // sendNotification(followee.getId(),getMessage(event,locale));


    }
}
