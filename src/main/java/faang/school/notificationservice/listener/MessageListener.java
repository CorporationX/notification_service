package faang.school.notificationservice.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public interface MessageListener {
    void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment);
}
