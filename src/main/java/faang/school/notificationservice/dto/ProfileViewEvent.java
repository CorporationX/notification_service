package faang.school.notificationservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProfileViewEvent {
    private Long viewerId;
    private Long authorId;
}
