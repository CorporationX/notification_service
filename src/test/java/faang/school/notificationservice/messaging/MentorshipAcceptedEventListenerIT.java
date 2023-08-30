package faang.school.notificationservice.messaging;


import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.mentorshiprequest.MentorshipAcceptedDto;
import faang.school.notificationservice.notification.EmailService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
@Testcontainers
public class MentorshipAcceptedEventListenerIT {

    @MockBean
    private UserServiceClient userServiceClient;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MentorshipAcceptedEventListener mentorshipAcceptedEventListener;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.5.3"));

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    static {
        kafka.start();
    }

    @BeforeEach
    void setUp() {
        Mockito.when(userServiceClient.getUser(2L)).thenReturn(mockUserDto());
    }

    @AfterAll
    static void tearDown() {
        kafka.stop();
    }

    @Test
    void listen_messageShouldBeLikeExpectedAndSent() {
        writeToTopic("mentorship-accepted", mockMentorshipAcceptedDto());

        Assertions.assertEquals("Congrats! Your mentorship request was accepted by Vladimir!",
                mentorshipAcceptedEventListener.getMessage(mockMentorshipAcceptedDto(), Locale.UK));
    }

    private KafkaProducer<String, MentorshipAcceptedDto> createProducer() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaProducer<>(props);
    }

    private void writeToTopic(String topicName, MentorshipAcceptedDto dto) {

        try (KafkaProducer<String, MentorshipAcceptedDto> producer = createProducer()) {
            ProducerRecord<String, MentorshipAcceptedDto> record = new ProducerRecord<>(topicName, dto);
            producer.send(record);
        }
    }

    private MentorshipAcceptedDto mockMentorshipAcceptedDto() {
        return MentorshipAcceptedDto.builder()
                .id(1L)
                .receiverId(1L)
                .requesterId(2L)
                .receiverUsername("Vladimir")
                .build();
    }

    private UserDto mockUserDto() {
        return UserDto.builder()
                .id(1L)
                .username("Andrey")
                .email("test@gmail.com")
                .phone("123")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }
}
