package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.LikePostEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikePostMessageBuilderTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private LikePostMessageBuilder likePostMessageBuilder;


    @Test
    void testBuildMessage() {
        LikePostEvent event = new LikePostEvent();
        event.setLikeAuthorId(1L);
        UserDto userDto = new UserDto();
        userDto.setUsername("JohnDoe");

        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(messageSource.getMessage(eq("like.post"), any(Object[].class), eq(Locale.ENGLISH)))
                .thenReturn("JohnDoe liked your post");

        String message = likePostMessageBuilder.buildMessage(event, Locale.ENGLISH);

        assertEquals("JohnDoe liked your post", message);
    }

    @Test
    void testGetSupportedClass() {
        Class<LikePostEvent> supportedClass = likePostMessageBuilder.getSupportedClass();

        assertEquals(LikePostEvent.class, supportedClass);
    }
}