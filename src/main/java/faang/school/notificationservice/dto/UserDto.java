package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    @JsonProperty("userId")
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
