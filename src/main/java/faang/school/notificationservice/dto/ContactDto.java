package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDto {
    private String contact;
    private ContactType type;

    public enum ContactType {
        GITHUB, TELEGRAM, VK, FACEBOOK, INSTAGRAM, WHATSAPP, CUSTOM
    }
}
