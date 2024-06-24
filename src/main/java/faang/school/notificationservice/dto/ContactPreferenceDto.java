package faang.school.notificationservice.dto;

import faang.school.notificationservice.entity.PreferredContact;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactPreferenceDto {

    private long id;

    @NotNull(message = "UserId should not be null")
    @Positive(message = "UserId should be positive")
    private Long userId;

    @NotNull(message = "Preference should not be null")
    private PreferredContact preference;
}
