package faang.school.notificationservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserNameDto {
    private String username;
    private UserDto.PreferredContact preference;
    private String email;
}