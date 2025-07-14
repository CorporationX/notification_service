package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MentorshipRequestDto {
    @NotNull
    private Long requesterId;
    @NotNull
    private Long receiverId;
    @NotBlank(message = "Description is required")
    @Size(min = 4, message = "Description must be at least 4 characters long")
    private String description;
    private String status;
}