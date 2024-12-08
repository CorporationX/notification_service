package faang.school.notificationservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import faang.school.notificationservice.deserializer.LocalDateTimeArrayDeserializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SubscribEventDto {
    private long followerId;
    private long followeeId;
    @JsonDeserialize(using = LocalDateTimeArrayDeserializer.class)
    private LocalDateTime eventTime;
}
