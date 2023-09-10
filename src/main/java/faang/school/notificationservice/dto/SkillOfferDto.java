package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillOfferDto {
    private Long id;
    @NotNull
    @Min(value = 1, message = "Please select skill")
    private Long skill;
    @NotNull
    private Long authorId;
    @NotNull
    @Min(value = 1, message = "Please select receiver")
    private Long receiverId;
}