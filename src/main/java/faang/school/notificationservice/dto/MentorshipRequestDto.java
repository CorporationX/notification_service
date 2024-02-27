package faang.school.notificationservice.dto;

import faang.school.notificationservice.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipRequestDto {

    private Long id;

    private String description;

    private Long requester;

    private Long receiver;

    private RequestStatus status;

    private String rejectionReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
