package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.TestContainersConfig;
import faang.school.notificationservice.model.LikeEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@EmbeddedKafka(topics = "${spring.kafka.topics.like.name}")
@SpringBootTest
@ActiveProfiles("test")
public class LikeListenerIt extends TestContainersConfig {
    @Value("${spring.kafka.topics.like.name}")
    private String likeTopic;

    @MockBean
    private UserServiceClient userServiceClient;

    @Autowired
    private KafkaTemplate<String, LikeEvent> likeEventKafkaTemplate;

    @Test
    public void onMessage_LikeListener() throws InterruptedException {
        LikeEvent likeEvent = LikeEvent.builder()
                .authorId(1L)
                .likerUsername("username")
                .postTitle("title")
                .build();
        likeEventKafkaTemplate.send(likeTopic, likeEvent);
        Thread.sleep(3000);
        verify(userServiceClient, atLeastOnce()).getUserNotificationDto(anyLong());
    }
}
