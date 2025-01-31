package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    private Long id;
    private String contact;
    private Long userId;
    private ContactType type;

    public enum ContactType {
        GITHUB, TELEGRAM, VK, FACEBOOK, INSTAGRAM, WHATSAPP, CUSTOM
    }
}
