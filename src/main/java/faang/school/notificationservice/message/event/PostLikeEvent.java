package faang.school.notificationservice.message.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostLikeEvent {

    private Long receiverId;
    private Long authorId;
    private String authorName;
    private Long postId;
    private LocalDateTime likeTime;
}
