package faang.school.notificationservice.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LikePostEvent {
    private Long postAuthorId;
    private Long likeAuthorId;
    private long postId;
}
