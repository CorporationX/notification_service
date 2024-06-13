package faang.school.notificationservice.dto;

import faang.school.notificationservice.entity.PreferredContact;
import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
}
