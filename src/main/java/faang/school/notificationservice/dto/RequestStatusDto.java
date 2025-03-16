package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.notificationservice.enums.RequestStatus;
import faang.school.notificationservice.enums.RequestType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestStatusDto {

    @NotNull(message = "createdBy cannot be null")
    private Long createdBy;

    @NotNull(message = "type cannot be null")
    private RequestType type;

    private RequestStatus requestStatus;

    private String statusDescription;
}
