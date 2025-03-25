package faang.school.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserNotificationDto {
    @Min(value = 1, message = "User's id must be greater than 0")
    private Long id;
    @Size(max = 64, message = "The length must not exceed 64 characters")
    private String username;
    @Email(message = "Please provide a valid email address")
    private String email;
    private String phone;
    public UserDto.PreferredContact preferredContact;
//    private ContactPreference contactPreference;
}
