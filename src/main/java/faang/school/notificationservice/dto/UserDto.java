package faang.school.notificationservice.dto;

import faang.school.notificationservice.enums.PreferredContact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private Locale locale;

    public PreferredContact getPreference() {
        return preference == null ? PreferredContact.EMAIL : preference;
    }
}