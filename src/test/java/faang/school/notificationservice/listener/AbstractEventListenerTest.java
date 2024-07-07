package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.relational.core.sql.Like;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {

    private AbstractEventListener<LikeEvent> abstractEventListener;
    private List<MessageBuilder<LikeEvent>> messageBuilders;
    private List<NotificationService> notificationServices;
    private ObjectMapper objectMapper;
    private UserServiceClient userServiceClient;
    private LikeEvent likeEvent;
    private Locale locale = Locale.UK;

    @BeforeEach
    public void init(){
         abstractEventListener = Mockito.mock(AbstractEventListener.class,
                Mockito.CALLS_REAL_METHODS);

        objectMapper = Mockito.mock(ObjectMapper.class);
        userServiceClient = Mockito.mock(UserServiceClient.class);
        LikeMessageBuilder likeMessageBuilder = Mockito.mock(LikeMessageBuilder.class);
        messageBuilders.add(likeMessageBuilder);
        EmailService emailService = Mockito.mock(EmailService.class);
        notificationServices.add(emailService);
        likeEvent = LikeEvent.builder()
                .authorId(1L)
                .userId(2L)
                .postId(1L)
                .build();

        when(emailService.getPreferredContact()).thenCallRealMethod();
        when(likeMessageBuilder.getInstance()).thenCallRealMethod();
        doNothing().when(likeMessageBuilder.buildMessage(likeEvent, locale));

        ReflectionTestUtils.setField(abstractEventListener, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(abstractEventListener, "messageBuilders", messageBuilders);
        ReflectionTestUtils.setField(abstractEventListener, "notificationServices", notificationServices);
        ReflectionTestUtils.setField(abstractEventListener, "userServiceClient", userServiceClient);
    }

    @Test
    public void testGetMessageWithGettingText(){

        abstractEventListener.getMessage(likeEvent, locale);

        messageBuilders.forEach(builder->verify(builder, times(1)).getInstance());
    }

}
