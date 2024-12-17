package faang.school.notificationservice.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ProjectFollowerEvent implements Serializable {
    private final long projectId;
    private final long followerId;
    private final long followeeId;
}
