package faang.school.notificationservice.dto;

import faang.school.notificationservice.entity.PreferredContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipOfferedEventDto {
    private Long receiverId;
    private Long requesterId;
    private String email;
    private LocalDateTime timestamp;
    private PreferredContact preferredContact;
    private Locale locale = null;
}
