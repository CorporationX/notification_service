package faang.school.notificationservice.dto;

import faang.school.notificationservice.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequestEvent {
    private Long userId;
    private RequestStatus requestStatus;
    private LocalDateTime requestCreatedTime;
}

