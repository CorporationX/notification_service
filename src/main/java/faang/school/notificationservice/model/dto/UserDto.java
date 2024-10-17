package faang.school.notificationservice.model.dto;
import faang.school.notificationservice.model.enums.PreferredContact;

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
