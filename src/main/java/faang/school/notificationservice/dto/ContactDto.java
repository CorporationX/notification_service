package faang.school.notificationservice.dto;

import faang.school.notificationservice.enums.ContactType;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Long userId;
    @NotNull
    private String contact;
    @NotNull
    private ContactType type;
}
