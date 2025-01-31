package faang.school.notificationservice.message.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {
    private long receiverId;
    private String commentContent;
    private String commentAuthorUserName;
}
