package faang.school.notificationservice.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipOfferedEventDto {
    private Long receiverId;
    private Long requesterId;
    @Size(max = 150)
    private String description;
}