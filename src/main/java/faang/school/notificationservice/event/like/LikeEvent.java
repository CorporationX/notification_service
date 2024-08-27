package faang.school.notificationservice.event.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeEvent {
    UUID eventId;
    private long authorId;
    private long receivedId;
    private long likeId;
}