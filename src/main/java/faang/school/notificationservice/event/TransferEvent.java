package faang.school.notificationservice.event;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class TransferEvent {

    @NotNull
    private Long senderId;

    @NotNull
    private Long receiverId;
}