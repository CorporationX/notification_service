package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestEvent;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class RecommendationRequestMessageTest {
    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;
    @InjectMocks
    private RecommendationRequestMessage recommendationRequestMessage;

    @Test
    public void testSupporterRecommendationRequestEventClass() {
        RecommendationRequestEvent recommendationRequestEvent = new RecommendationRequestEvent(1L, 2L, 3L);
        Class<RecommendationRequestEvent> recommendationRequestEventClass = recommendationRequestMessage.supportsEventType();
        Assertions.assertEquals(recommendationRequestEvent.getClass(), recommendationRequestEventClass);
    }

    @Test
    public void testUnSupporterRecommendationRequestEventClass() {
        Object recommendationRequestEvent = new Object();
        Class<RecommendationRequestEvent> recommendationRequestEventClass = recommendationRequestMessage.supportsEventType();
        Assertions.assertNotEquals(recommendationRequestEvent.getClass(), recommendationRequestEventClass);
    }

//    @Test
//    public void testSuccessBuildMessage() {
//        RecommendationRequestEvent recommendationRequestEvent = new RecommendationRequestEvent(1L, 2L, 3L);
//        UserDto userDto = new UserDto();
//        userDto.setId(2L);
//        userDto.setUsername("Sam");
//
//        Locale locale = Locale.ENGLISH;
//        String text = "You've got a new recommendation from Sam";
//        Mockito.when(userServiceClient.getUser(2L)).thenReturn(userDto);
//        Mockito.when(userDto.getUsername()).thenReturn("Sam");
//        Mockito.when(messageSource.getMessage("recommendationRequest.new", new Object[]{userDto.getUsername()}, locale)).thenReturn(text);
//
//        String s = recommendationRequestMessage.buildMessage(recommendationRequestEvent, locale);
//
//        Mockito.verify(userServiceClient, times(1)).getUser(2L);
//        Mockito.verify(messageSource, times(1)).getMessage("recommendationRequest.new", new Object[]{userDto.getUsername()}, locale);
//        Assertions.assertEquals(text, s);
//    }
}
