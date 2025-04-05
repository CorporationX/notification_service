package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.dto.PostShortContentDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.springframework.mail.MailSender;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MailSender mailSender;

    @Mock
    private Message redisMessage;


    @InjectMocks
    private LikeEventListener likeEventListener;

    private LikePostEvent testEvent;
    private UserDto testUserDto;
    private PostShortContentDto testPostDto;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        testEvent = new LikePostEvent();
        testEvent.setPostId(1L);
        testEvent.setPostAuthorId(2L);
        testEvent.setLikeUserId(3L);

        testUserDto = new UserDto();
        testUserDto.setId(2L);
        testUserDto.setUsername("testUser");
        testUserDto.setEmail("test@example.com");
        testUserDto.setPreference(UserDto.PreferredContact.EMAIL);

        testPostDto = new PostShortContentDto();
        testPostDto.setShortContent("Test post content");

        emailService = new EmailService(mailSender);
    }

    @Test
    void testOnMessageShouldThrowRuntimeExceptionWhenJsonInvalid() throws IOException {
        when(redisMessage.getBody()).thenReturn("invalid json".getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(LikePostEvent.class)))
                .thenThrow(new JsonProcessingException("Invalid JSON") {});

        assertThrows(RuntimeException.class, () ->
                likeEventListener.onMessage(redisMessage, null));
    }
}
