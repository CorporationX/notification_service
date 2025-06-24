package faang.school.notificationservice.event.kafka;


import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.SubscriptionEventType;
import faang.school.notificationservice.event.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class SubscriptionEvent implements NotificationEvent {
    private SubscriptionEventType subscriptionEventType;
    private UserDto owner;
    private UserDto follower;
}