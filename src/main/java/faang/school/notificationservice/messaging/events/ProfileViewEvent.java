package faang.school.notificationservice.messaging.events;

import faang.school.notificationservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileViewEvent {
    private Long idVisitor;
    private Long idVisited;
    private UserDto.PreferredContact preferredContact;
}
