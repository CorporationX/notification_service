package faang.school.notificationservice.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private Locale locale;
    private PreferredContact notifyPreference;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM;

        @JsonCreator
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
