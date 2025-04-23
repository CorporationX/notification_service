package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private Long telegramChatId;

    @Builder.Default
    private PreferredContact preference = PreferredContact.EMAIL;

    @Builder.Default
    private Locale locale = Locale.ENGLISH;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
