package faang.school.notificationservice.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FollowerEvent {
    Long subscriberId;
    Long followerId;
}

