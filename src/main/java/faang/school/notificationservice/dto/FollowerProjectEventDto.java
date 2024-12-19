package faang.school.notificationservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import faang.school.notificationservice.deserializer.LocalDateTimeArrayDeserializer;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FollowerProjectEventDto {
    long followerId;
    long projectId;
    long ownerId;
    @JsonDeserialize(using = LocalDateTimeArrayDeserializer.class)
    private LocalDateTime eventTime;
}
