package faang.school.notificationservice.dto;

import faang.school.notificationservice.model.PreferredContact;
import lombok.Builder;
import lombok.Data;

@Data

public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preferrence;

}
