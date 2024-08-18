package faang.school.notificationservice.event.achievement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import faang.school.notificationservice.event.Event;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@Jacksonized
public class AchievementEvent implements Event {
    /**
     * ID of the user who received the achievement
     */
    private final long userId;
    /**
     * ID of record in {@link UserAchievementRepository}. ID achievement for a specific user.
     */
    private final long userAchievementId;
    /**
     * ID achievement in {@link AchievementRepository}
     */
    private final long achievementId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime receiveAt;
}
