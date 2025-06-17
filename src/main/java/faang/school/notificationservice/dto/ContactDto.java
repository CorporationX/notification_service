package faang.school.notificationservice.dto;

import faang.school.notificationservice.dto.UserDto.PreferredContact;
import lombok.Data;

@Data
public class ContactDto {
    private String contact;
    private PreferredContact type;
}
