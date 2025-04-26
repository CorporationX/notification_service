package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    @JsonIgnore
    private Long telegramChatId;

    @Builder.Default
    private PreferredContact preference = PreferredContact.EMAIL;

    @Builder.Default
    private Locale locale = Locale.ENGLISH;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}