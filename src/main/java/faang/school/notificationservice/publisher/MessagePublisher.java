package faang.school.notificationservice.publisher;

import org.springframework.http.ResponseEntity;

public interface MessagePublisher<T> {
    ResponseEntity<String> publish(T message);
}
