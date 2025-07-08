package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class ProfileViewDto {
    private UserDto profileUser;
    private UserDto viewerUser;
    private LocalDateTime viewingTime;
}
