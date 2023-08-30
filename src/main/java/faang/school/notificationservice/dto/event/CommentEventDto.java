package faang.school.notificationservice.dto.event;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentEventDto {
    private long idComment;
    private String contentComment;
    private long authorIdComment;
    private Long postId;
    private Long postAuthorId;
}