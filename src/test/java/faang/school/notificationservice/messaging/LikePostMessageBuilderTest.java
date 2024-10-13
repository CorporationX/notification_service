package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.LikePostEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikePostMessageBuilderTest {

    @InjectMocks
    private LikePostMessageBuilder likePostMessageBuilder;

    @Mock
    private MessageSource messageSource;

    private static final long ID = 1L;
    private static final String EXPECTED_MESSAGE = "Your post 1 was liked!";

    @Test
    @DisplayName("Успешное возвращение LikePostEvent.class")
    public void whenGetInstanceReturnLikePostEvent() {
        assertEquals(LikePostEvent.class, likePostMessageBuilder.getInstance());
    }

    @Test
    @DisplayName("Успешное построение сообщения")
    public void whenBuildMessageReturnExpectedMessage() {
        LikePostEvent event = LikePostEvent.builder()
                .postId(ID)
                .build();
        Locale locale = Locale.UK;
        when(messageSource.getMessage("like.post", new Object[]{ID}, locale)).thenReturn(EXPECTED_MESSAGE);

        String actualMessage = likePostMessageBuilder.buildMessage(event, locale);

        assertEquals(EXPECTED_MESSAGE, actualMessage);
        verify(messageSource).getMessage("like.post", new Object[]{ID}, locale);
    }
}