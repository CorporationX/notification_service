package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class RecommendationEventBuilder {
    private UserDto author;
    private UserDto receiver;
    private Recommendation recommendation;
}
