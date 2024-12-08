package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserProfileSettingsDto {
    private Long id;
    private Long userId;

    @JsonProperty("preference")
    private String preference;

    @JsonCreator
    public UserProfileSettingsDto(@JsonProperty("preference") String preference) {
        this.preference = preference;
    }
}
