package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileViewEvent {
    @NotNull
    private Long observerId;
    @NotNull
    private Long observedId;
    @NotNull
    private LocalDateTime viewedAt;
}
