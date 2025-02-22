package faang.school.notificationservice.handler;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.exception.impl.non_retryable.ListSizeNotOneException;
import faang.school.notificationservice.exception.impl.non_retryable.NotFoundElementException;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageHandlerTest {

    @Mock
    private MessageBuilder<TestDto> testMessageBuilder;

    private MessageHandler<TestDto> messageHandler;

    private TestDto testDto;
    private UserServiceDto userServiceDto;
    private List<String> additionalWords;

    @BeforeEach
    void setUp() {
        testDto = new TestDto();
        userServiceDto = new UserServiceDto();
        additionalWords = new ArrayList<>();
        messageHandler = new MessageHandler<>(List.of(testMessageBuilder));

    }

    @Test
    void getMessageSingleBuilderFound() {
        when(testMessageBuilder.buildMessage(testDto, userServiceDto, additionalWords)).thenReturn("Test message");
        when(testMessageBuilder.getInstance()).thenAnswer(invocation -> TestDto.class);

        String result = messageHandler.getMessage(testDto, userServiceDto, additionalWords);

        assertEquals("Test message", result);
        verify(testMessageBuilder).buildMessage(testDto, userServiceDto, additionalWords);
    }

    @Test
    void getMessageNoBuilderFoundThrowsNotFoundElementException() {
        MessageHandler<AnotherTestDto> handler = new MessageHandler<>(List.of());
        AnotherTestDto anotherTestDto = new AnotherTestDto();

        assertThrows(NotFoundElementException.class, () -> handler.getMessage(anotherTestDto, userServiceDto, additionalWords));
    }

    @Test
    void getMessageMultipleBuildersFoundThrowsListSizeNotOneException() {
        when(testMessageBuilder.getInstance()).thenAnswer(invocation -> TestDto.class);
        MessageBuilder<TestDto> anotherTestMessageBuilder = mock(MessageBuilder.class);
        when(anotherTestMessageBuilder.getInstance()).thenAnswer(invocation -> TestDto.class);

        MessageHandler<TestDto> handler = new MessageHandler<>(List.of(testMessageBuilder, anotherTestMessageBuilder));

        assertThrows(ListSizeNotOneException.class, () -> handler.getMessage(testDto, userServiceDto, additionalWords));
    }

    static class TestDto {
    }

    static class AnotherTestDto {
    }
}