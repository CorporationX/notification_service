package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record UserDto(
        Long id,
        String username,
        String email,
        String phone,
        String aboutMe,
        String locale,
        String preference
) {
    public PreferredContact preferredContact() {
        return PreferredContact.fromString(preference);
    }

    @Getter
    public enum PreferredContact {
        EMAIL,
        PHONE,
        TELEGRAM;

        public static PreferredContact fromString(String preference) {
            for (PreferredContact contact : PreferredContact.values()) {
                if (contact.name().equalsIgnoreCase(preference)) {
                    return contact;
                }
            }
            throw new IllegalArgumentException("No contact preference with name " + preference + " found");
        }
    }
}