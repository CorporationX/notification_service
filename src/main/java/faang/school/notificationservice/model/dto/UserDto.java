package faang.school.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.notificationservice.model.enums.PreferredContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
}
