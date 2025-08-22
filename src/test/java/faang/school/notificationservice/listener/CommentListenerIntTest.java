package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.kafka.KafkaProperty;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.CommentEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
class CommentListenerIntTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaProperty kafkaProperty;
    @SpyBean
    private CommentListener commentListener;
    @Captor
    private ArgumentCaptor<String> dataCaptor;
    @MockBean
    private UserServiceClient client;

    @Test
    @DisplayName("Успешное чтение event из Kafka")
    void positive_consumerCommentEventReceived() throws Exception {
        CommentEvent event = createCommentEvent();
        String expected = objectMapper.writeValueAsString(event);
        when(client.getUser(any(Long.class))).thenReturn(createUserDto());

        kafkaTemplate.send(kafkaProperty.topic().commentNew(), expected);

        verify(commentListener, timeout(5000).times(1)).consume(dataCaptor.capture());
        String actual = dataCaptor.getValue();
        assertEquals(expected, actual);
    }

    private CommentEvent createCommentEvent() {
        return CommentEvent.builder()
                .id(1)
                .postAuthorId(2)
                .commentAuthorId(3)
                .postId(4)
                .content("comment content")
                .build();
    }

    private UserDto createUserDto() {
        return UserDto.builder()
                .id(1)
                .username("Username")
                .phone("123456")
                .preference(UserDto.PreferredContact.PHONE)
                .build();
    }
}