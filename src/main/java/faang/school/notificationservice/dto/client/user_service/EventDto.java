package faang.school.notificationservice.dto.client.user_service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private int maxAttendees;
    private List<Long> attendeeIds;
    private List<Long> ratingIds;
    private Long ownerId;
    private List<Long> relatedSkillIds;
    private EventType type;
    private EventStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum EventType {
        WEBINAR("Webinar"),
        POLL("Poll"),
        MEETING("Meeting"),
        GIVEAWAY("Giveaway"),
        PRESENTATION("Presentation");
        private final String type;

        EventType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return type;
        }
    }

    public enum EventStatus {
        PLANNED("Planned"),
        IN_PROGRESS("In Progress"),
        CANCELED("Canceled"),
        COMPLETED("Completed");
        private final String status;

        EventStatus(String type) {
            this.status = type;
        }

        public String getMessage() {
            return status;
        }
    }
}
