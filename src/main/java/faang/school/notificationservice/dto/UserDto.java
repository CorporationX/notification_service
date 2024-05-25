package faang.school.notificationservice.dto;

import faang.school.notificationservice.enums.PreferredContact;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
}
