package faang.school.notificationservice.listener;

import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final EmailService emailService;

    @KafkaListener(topics = "notification.dispatch.v1", groupId = "mentorship.request.created.v1")
    public void handleNotificationEvent(NotificationEvent event){
        emailService.send(event.getUserDto());
    }
}
