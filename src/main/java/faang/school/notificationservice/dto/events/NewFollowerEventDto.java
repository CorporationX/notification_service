package faang.school.notificationservice.dto.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public record NewFollowerEventDto(
        long actorId,
        long receiverId,
        String followerDisplayName
) {
    @JsonIgnore
    public String getKey() {
        return Long.toString(receiverId);
    }
}