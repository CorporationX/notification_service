package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Locale;

@Builder
public record FollowerEvent(
        @JsonProperty("follower_id") Long followerId,
        @JsonProperty("followee_id") Long followeeId,
        @JsonProperty("follow_time") LocalDateTime followTime,
        @JsonProperty("locale") Locale locale
) {
}
