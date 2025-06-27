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
            Long viewedUserId = jsonNode.get("viewedUserId").asLong();
            Long viewerUserId = jsonNode.get("viewerUserId").asLong();

            sendNotification(viewedUserId, viewerUserId);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(Long viewedUserId, Long viewerUserId) {
        System.out.printf("User %d viewed profile of user %d%n", viewerUserId, viewedUserId);
    }
}
