package faang.school.notificationservice.builder;

import faang.school.notificationservice.model.event.LikeEvent;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

@ExtendWith(MockitoExtension.class)

class LikeMessageBuilderTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private LikeMessageBuilder messageBuilder;

    private LikeEvent likeEvent;

    @BeforeEach
    void setUp() {
        likeEvent = LikeEvent.builder()
                .build();
    }

    @Test
    void buildMessageOk() {
        //given
        var message = "Hello bro";
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn("Hello bro");

        //when
        var result = messageBuilder.buildMessage(likeEvent, Locale.getDefault());

        //then
        Assertions.assertEquals(message, result);
    }
}