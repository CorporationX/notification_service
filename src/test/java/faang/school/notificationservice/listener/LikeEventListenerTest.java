package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.PreferredContact;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.listener.like.LikeEventListener;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {

    private List<MessageBuilder<LikeEvent>> messageBuilders;
    private List<NotificationService> notificationServices;
    private LikeEventListener likeEventListener;
    private ObjectMapper objectMapper;
    private LikeEvent likeEvent;
    private UserDto userDto;

    @BeforeEach
    void init() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        UserServiceClient userServiceClient = Mockito.mock(UserServiceClient.class);
        LikeMessageBuilder likeMessageBuilder = Mockito.mock(LikeMessageBuilder.class);
        messageBuilders.add(likeMessageBuilder);
        EmailService emailService = Mockito.mock(EmailService.class);
        notificationServices.add(emailService);
        likeEventListener = new LikeEventListener(objectMapper, messageBuilders, notificationServices, userServiceClient);

        likeEvent = LikeEvent.builder().authorId(1L).userId(2L).postId(1L).build();

        userDto = UserDto.builder().id(1L).preference(PreferredContact.EMAIL).build();

        when(likeMessageBuilder.getInstance()).thenCallRealMethod();
        doNothing().when(likeMessageBuilder.buildMessage(eq(likeEvent), any(Locale.class)));
        when(emailService.getPreferredContact()).thenCallRealMethod();
        //doNothing().when(emailService).send(userDto, );
        when(userServiceClient.getUser(likeEvent.getAuthorId())).thenReturn(userDto);
    }

    @Test
    public void testOnMessageWithSuccessfulHandlingEvent() throws IOException {

        byte[] body = new byte[]{2};
        Message message = new DefaultMessage(new byte[]{1}, body);
        byte[] pattern = new byte[]{3};

        //verify(likeEventListener, times(1)).handleEvent();

        likeEventListener.onMessage(message, pattern);
    }
}
