package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private Long telegramId;
    private PreferredContact preference;
    private List<Long> mentorIds;
    private List<Long> menteeIds;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}