package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserKafkaListener {

    public List<NotificationService> notificationServices;
    private UserServiceClient userServiceClient;
    @KafkaListener(topics = "view-topic", groupId = "${kafka.consumer.group-id}")
    public void consume(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(message);
            String profileUser = jsonNode.get("profileUser").asText();
            String viewerUser = jsonNode.get("viewerUser").asText();

            sendNotification(profileUser, viewerUser);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String profileUser, String viewerUser) {
        System.out.printf("User %s viewed profile of user %s%n", viewerUser, profileUser);
    }
}
