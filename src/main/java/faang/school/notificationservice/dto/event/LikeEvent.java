package faang.school.notificationservice.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import faang.school.notificationservice.properties.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeEvent {
    private long postId;
    private long authorId;
    private long userId;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime likedAt;
    private EventType type;
}
