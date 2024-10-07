package faang.school.notificationservice.dto;

import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class EmailRequestDto {

    private String email;

    private String text;

    private String subject;
}
