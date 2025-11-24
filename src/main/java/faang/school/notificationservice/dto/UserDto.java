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
        EMAIL(0),
        PHONE(1),
        TELEGRAM(2);

        private final int code;

        PreferredContact(int code) {
            this.code = code;
        }

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