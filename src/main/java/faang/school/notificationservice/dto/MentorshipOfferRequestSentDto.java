package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@RequiredArgsConstructor
@Validated
public class MentorshipOfferRequestSentDto {
    @NotNull
    private Long id;

    @NotNull
    private Long requesterId;

    @NotNull
    private Long receiverId;
}
