package faang.school.notificationservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageCode {
    COMMENT_NEW("comment.new"),
    SKILL_OFFER("skill-offer");

    private final String code;
}
