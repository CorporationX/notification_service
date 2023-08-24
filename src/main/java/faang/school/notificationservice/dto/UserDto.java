package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private Long countryId;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM;
    }
}
