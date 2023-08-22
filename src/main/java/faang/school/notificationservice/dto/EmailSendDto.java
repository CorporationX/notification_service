package faang.school.notificationservice.dto;

import faang.school.notificationservice.dto.user.UserDto;
import lombok.Data;

@Data
public class EmailSendDto {
    private UserDto userDto;
    private String message;
}
