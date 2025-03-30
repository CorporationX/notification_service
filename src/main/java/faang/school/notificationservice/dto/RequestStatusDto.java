package faang.school.notificationservice.dto;

import faang.school.notificationservice.enums.RequestStatus;
import faang.school.notificationservice.enums.RequestType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record RequestStatusDto(

        @NotNull(message = "createdBy cannot be null")
        Long createdBy,

        @NotNull(message = "type cannot be null")
        RequestType type,

        @NotNull(message = "type cannot be requestStatus")
        RequestStatus requestStatus,

        String statusDescription
) {
}
