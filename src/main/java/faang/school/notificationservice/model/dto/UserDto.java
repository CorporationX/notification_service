package faang.school.notificationservice.model.dto;

import faang.school.notificationservice.model.enums.PreferredContact;
import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private boolean active;
    private PreferredContact preference;
}
