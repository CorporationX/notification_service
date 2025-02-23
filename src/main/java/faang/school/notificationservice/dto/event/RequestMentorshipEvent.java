package faang.school.notificationservice.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestMentorshipEvent {
    @JsonProperty("mentorId")
    private long mentorId;

    @JsonProperty("menteeId")
    private long menteeId;
}
