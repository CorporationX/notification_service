package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UserDto {
    @NotNull
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String phone;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
