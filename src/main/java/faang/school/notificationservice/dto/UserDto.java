package faang.school.notificationservice.dto;

import faang.school.notificationservice.enums.PreferredContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Locale;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private Locale locale;

    public PreferredContact getPreference() {
        return preference == null ? PreferredContact.EMAIL : preference;
    }
}