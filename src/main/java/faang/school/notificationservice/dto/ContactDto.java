package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class ContactDto {
    private String contact;
    private ContactType type;

    public enum ContactType {
        GITHUB, TELEGRAM, VK, FACEBOOK, INSTAGRAM, WHATSAPP, CUSTOM
    }
}
