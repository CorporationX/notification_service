package faang.school.notificationservice.message.event;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RecommendationReceivedEvent extends NotificationEvent {
    private long recommenderUserId;
    private long recommendationId;
}