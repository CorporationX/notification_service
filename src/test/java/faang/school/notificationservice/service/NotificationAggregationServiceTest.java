package faang.school.notificationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.model.PendingNotifications;
import faang.school.notificationservice.service.notification.EventType;
import faang.school.notificationservice.service.notification.NotificationAggregationService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationAggregationServiceTest {

    private final NotificationAggregationService aggregationService =
            new NotificationAggregationService();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testNotificationsAggregatedCorrectly() {
        JsonNode eventData1 = createEventNode(
                2L,
                "JaneSmith",
                123L,
                "First content...",
                "JohnDoe"
        );
        JsonNode eventData2 = createEventNode(
                3L,
                "JohnSmith",
                123L,
                "Second content...",
                "JaneDoe"
        );
        JsonNode eventData3 = createEventNode(
                4L,
                "Alice",
                456L,
                "Third content...",
                "Bob"
        );

        PendingNotifications notification1 = PendingNotifications.builder()
                .receiverId(42L)
                .targetEntityId(123L)
                .eventData(eventData1)
                .eventType(EventType.COMMENT_LIKED)
                .build();

        PendingNotifications notification2 = PendingNotifications.builder()
                .receiverId(42L)
                .targetEntityId(123L)
                .eventData(eventData2)
                .eventType(EventType.COMMENT_LIKED)
                .build();

        PendingNotifications notification3 = PendingNotifications.builder()
                .receiverId(42L)
                .targetEntityId(456L)
                .eventData(eventData3)
                .eventType(EventType.POST_LIKED)
                .build();

        List<PendingNotifications> inputNotifications = List.of(
                notification1,
                notification2,
                notification3
        );

        List<AggregatedNotificationsDto> aggregated = aggregationService
                .aggregateNotifications(inputNotifications);

        assertEquals(2, aggregated.size());

        AggregatedNotificationsDto group1 = aggregated.stream()
                .filter(dto -> dto.getTargetEntityId().equals(123L))
                .findFirst()
                .orElseThrow();
        assertEquals(42L, group1.getReceiverId());
        assertEquals(123L, group1.getTargetEntityId());
        assertEquals(EventType.COMMENT_LIKED, group1.getEventType());
        assertEquals(2, group1.getNotificationCount());
        assertEquals(eventData1, group1.getEventData());

        AggregatedNotificationsDto group2 = aggregated.stream()
                .filter(dto -> dto.getTargetEntityId().equals(456L))
                .findFirst()
                .orElseThrow();
        assertEquals(42L, group2.getReceiverId());
        assertEquals(456L, group2.getTargetEntityId());
        assertEquals(EventType.POST_LIKED, group2.getEventType());
        assertEquals(1, group2.getNotificationCount());
        assertEquals(eventData3, group2.getEventData());
    }

    private JsonNode createEventNode(long ownerId, String username, long postId, String content, String likerUsername) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode ownerNode = rootNode.putObject("owner");
        ownerNode.put("id", ownerId);
        ownerNode.put("email", username.toLowerCase() + "@example.com");
        ownerNode.put("phone", "1234567890");
        ownerNode.put("locale", "en");
        ownerNode.put("username", username);
        ownerNode.put("preference", "EMAIL");
        rootNode.put("postId", postId);
        rootNode.put("shortContent", content);
        rootNode.put("likerUsername", likerUsername);
        return rootNode;
    }
}