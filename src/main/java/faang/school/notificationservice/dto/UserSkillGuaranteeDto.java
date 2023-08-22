package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillGuaranteeDto {
    private Long id;
    private Long userId;
    private Long skillId;
    private Long guarantorId;
}
