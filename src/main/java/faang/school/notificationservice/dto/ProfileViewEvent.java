package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileViewEvent {
    private long viewerUserId;
    private long viewedUserId;
    private LocalDateTime receivedAt;
}