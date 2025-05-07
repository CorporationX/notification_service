package faang.school.notificationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestResponseDto {
    private Long userId;
    private Map<String, Object> inputData;
    private RequestTypeDto type;
    private RequestStatusDto status;
    private String statusDetails;
}
