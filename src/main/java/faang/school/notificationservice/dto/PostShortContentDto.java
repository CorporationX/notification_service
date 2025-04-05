package faang.school.notificationservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostShortContentDto {
    @Size(max = 50, message = "The length must not exceed 50 characters")
    private String shortContent;
}
