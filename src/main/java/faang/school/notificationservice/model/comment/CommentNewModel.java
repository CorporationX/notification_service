package faang.school.notificationservice.model.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentNewModel {
    private String postTitle;
    private String commentContent;
    private String usernameAuthorComment;
}
