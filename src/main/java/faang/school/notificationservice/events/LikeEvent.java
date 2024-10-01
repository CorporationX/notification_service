package faang.school.notificationservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeEvent implements Notifiable {
    private Long postId;
    private Long authorId;
    private Long userId;

    @Override
    public long getReceiverId() {
        return authorId;
    }
}
