package faang.school.notificationservice.dto;

import faang.school.notificationservice.entity.ContactType;
import jakarta.validation.constraints.NotBlank;
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
public class ContactDto {

    private long id;

    @NotNull(message = "UserId should not be null")
    @Positive(message = "UserID should be positive")
    private Long userId;

    @NotNull(message = "Contact should not be null")
    @NotBlank(message = "Contact should not be blank")
    private String contact;

    @NotNull(message = "ContactType should not be null")
    private ContactType type;
}
