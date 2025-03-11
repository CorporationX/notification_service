package faang.school.notificationservice.dto;

import lombok.Data;

import java.util.Locale;

@Data
public class UserEventDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PregerredContactNotification preference;
    private Locale locale;

}
