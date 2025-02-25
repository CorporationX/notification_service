package faang.school.notificationservice.service.comment;

import faang.school.notificationservice.dto.event.CommentEvent;

public interface CommentService {

    void sendNotification(CommentEvent commentEvent);
}
