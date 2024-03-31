package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private long id;
    @JsonIgnore(value = false)
    private long telegramChatId;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
