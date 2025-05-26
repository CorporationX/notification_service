package faang.school.notificationservice.dto;

import java.util.Objects;

public record FollowEventDto(long followerId, long followeeId) {

    @Override
    public String toString() {
        return "FollowEventDto[" +
                "followerId=" + followerId + ", " +
                "followeeId=" + followeeId + ']';
    }

}
