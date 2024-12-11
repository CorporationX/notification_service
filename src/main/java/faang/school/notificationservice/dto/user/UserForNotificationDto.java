package faang.school.notificationservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Locale;

@Builder
public record UserForNotificationDto(
        @JsonProperty("id")
        long id,

        @JsonProperty("username")
        String username,

        @JsonProperty("email")
        String email,

        @JsonProperty("phone")
        String phone,

        @JsonProperty("language")
        Language language,

        @JsonProperty("preference")
        PreferredContact preference
) {

    public Locale getLocaleFromLanguage() {
        return Locale.forLanguageTag(language.getTag());
    }

    public boolean isSamePreferredContact(PreferredContact preference) {
        return this.preference == preference;
    }
}
