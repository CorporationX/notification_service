package faang.school.notificationservice.dto;

import com.vonage.client.voice.ncco.SpeechSettings;
import lombok.Builder;

@Builder
public record UserForNotificationDto(
        long id,
        String username,
        String email,
        String phone,
        SpeechSettings.Language locale,
        PreferredContact preference
) {
}
