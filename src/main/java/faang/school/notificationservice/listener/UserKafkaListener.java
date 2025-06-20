package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserKafkaListener {
    @KafkaListener(topics = "view-topic", containerFactory = "kafkaListenerContainerFactory")
    public void listen(UserDto userDto) {
        System.out.println("Your profile was viewed by a user " + userDto.getUsername());
    }
}
