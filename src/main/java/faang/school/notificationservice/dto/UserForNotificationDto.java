package faang.school.notificationservice.dto;

import lombok.Builder;

import java.util.Locale;

@Builder
public record UserForNotificationDto(
        long id,
        String username,
        String email,
        String phone,
        Language locale,
        PreferredContact preference
) {

    public Locale getLocaleFromLanguage() {
        return Locale.forLanguageTag(locale.getTag());
    }

    public boolean isSamePreferredContact(PreferredContact preference) {
        return this.preference == preference;
    }
}
