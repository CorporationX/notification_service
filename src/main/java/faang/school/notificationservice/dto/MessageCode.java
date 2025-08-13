package faang.school.notificationservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageCode {
    COMMENT_NEW("comment.new");

    private final String code;
}
