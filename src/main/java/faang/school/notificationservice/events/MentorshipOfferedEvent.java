package faang.school.notificationservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipOfferedEvent implements Notifiable{
    private long authorId;
    private long mentorId;
    private long requestId;

    @Override
    public long getReceiverId() {
        return mentorId;
    }
}
