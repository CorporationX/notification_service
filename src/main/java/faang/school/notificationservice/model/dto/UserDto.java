package faang.school.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.notificationservice.model.enums.PreferredContact;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private String telegramUsername;
    private String telegramUserId;
    private PreferredContact preference;
}
