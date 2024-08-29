package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private long id;
    private String username;
    @NotBlank
    @Size(min = 8, max = 128)
    private String password;
    @NotNull
    private Long country;
    private String email;
    private String phone;
    private boolean isActive;
    private PreferredContact preference;
    private MultipartFile multipartFile;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
