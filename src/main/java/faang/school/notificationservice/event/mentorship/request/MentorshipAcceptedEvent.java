package faang.school.notificationservice.event.mentorship.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MentorshipAcceptedEvent {

    UUID eventId;
    long mentorshipRequestId;
    long requesterId;
    long receiverId;
}
