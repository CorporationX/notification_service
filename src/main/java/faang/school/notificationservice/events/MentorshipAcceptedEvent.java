package faang.school.notificationservice.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MentorshipAcceptedEvent implements Notifiable {

    private long requesterId;

    private long receiverId;

    private long mentorshipRequestId;

    @Override
    public long getReceiverId() {
        return requesterId;
    }

}
