package faang.school.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoalDto {
    private long id;
    private String title;
    private String description;
}
