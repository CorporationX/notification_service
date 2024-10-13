package faang.school.notificationservice.test_data.comment;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.comment.CommentEventDto;
import lombok.Getter;

@Getter
public class TestDataCommentEvent {
    public CommentEventDto getCommentEventDto() {
        return CommentEventDto.builder()
                .commentId(1L)
                .commentAuthorId(2L)
                .postId(11L)
                .postAuthorId(12L)
                .commentContent("TestContent")
                .build();
    }

    public UserDto getPostAuthor() {
        return UserDto.builder()
                .id(12L)
                .username("User1")
                .email("user1@mail.com")
                .phone("8-800-555-35-35")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }
}
