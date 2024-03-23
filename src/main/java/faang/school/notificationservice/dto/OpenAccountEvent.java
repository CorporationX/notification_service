package faang.school.notificationservice.dto;

import faang.school.notificationservice.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAccountEvent {
    @NotNull
    private Long userId;

    @NotNull
    private AccountStatus accountStatus;

    @NotNull
    private LocalDateTime accountOpenedTime;
}
