package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.notificationservice.entity.PreferredContact;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preferredContact;
}
