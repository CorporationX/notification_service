package faang.school.notificationservice.service;


import faang.school.notificationservice.exception.MessageBuilderException;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageBuilderUtilsTest {
    @Mock
    private MessageBuilder<TestEvent> suitableMessageBuilder;

    @Test
    public void getMessage_whenSuitableBuilderExists_shouldSuccess() {
        TestEvent event = new TestEvent();
        Locale locale = Locale.ENGLISH;
        String expectedMessage = "Test message";

        when(suitableMessageBuilder.getInstance()).thenReturn((Class) TestEvent.class);
        when(suitableMessageBuilder.buildMessage(event, locale)).thenReturn(expectedMessage);

        MessageBuilderUtils<TestEvent> utils = new MessageBuilderUtils<>(List.of(suitableMessageBuilder));

        String actualMessage = utils.getMessage(event, locale);

        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(suitableMessageBuilder).getInstance();
        verify(suitableMessageBuilder).buildMessage(event, locale);
    }

    @Test
    public void getMessage_whenMultipleBuildersExistAndFirstIsSuitable_shouldUseFirstSuitableBuilder() {
        TestEvent event = new TestEvent();
        Locale locale = Locale.ENGLISH;
        String expectedMessage = "Test message from first builder";

        MessageBuilder<TestEvent> anotherSuitableBuilder = mock(MessageBuilder.class);

        when(suitableMessageBuilder.getInstance()).thenReturn((Class) TestEvent.class);
        when(suitableMessageBuilder.buildMessage(event, locale)).thenReturn(expectedMessage);

        MessageBuilderUtils<TestEvent> utils = new MessageBuilderUtils<>(
                List.of(suitableMessageBuilder, anotherSuitableBuilder));

        String actualMessage = utils.getMessage(event, locale);

        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(suitableMessageBuilder).getInstance();
        verify(suitableMessageBuilder).buildMessage(event, locale);
        verifyNoInteractions(anotherSuitableBuilder);
    }

    @Test
    public void getMessage_whenEmptyBuildersList_shouldThrowException() {
        TestEvent event = new TestEvent();
        Locale locale = Locale.ENGLISH;

        MessageBuilderUtils<TestEvent> utils = new MessageBuilderUtils<>(List.of());

        MessageBuilderException exception = assertThrows(MessageBuilderException.class,
                () -> utils.getMessage(event, locale));

        Assertions.assertEquals("There is no suitable builder for this class, " + event.getClass(),
                exception.getMessage());
    }

    @Test
    public void getMessage_whenBuilderReturnsNull_shouldReturnNull() {
        TestEvent event = new TestEvent();
        Locale locale = Locale.ENGLISH;

        when(suitableMessageBuilder.getInstance()).thenReturn((Class) TestEvent.class);
        when(suitableMessageBuilder.buildMessage(event, locale)).thenReturn(null);

        MessageBuilderUtils<TestEvent> utils = new MessageBuilderUtils<>(List.of(suitableMessageBuilder));

        String actualMessage = utils.getMessage(event, locale);

        Assertions.assertNull(actualMessage);
        verify(suitableMessageBuilder).getInstance();
        verify(suitableMessageBuilder).buildMessage(event, locale);
    }

    @Test
    public void getMessage_whenLocaleIsNull_shouldHandleNullLocale() {
        TestEvent event = new TestEvent();
        String expectedMessage = "Test message with null locale";

        when(suitableMessageBuilder.getInstance()).thenReturn((Class) TestEvent.class);
        when(suitableMessageBuilder.buildMessage(event, null)).thenReturn(expectedMessage);

        MessageBuilderUtils<TestEvent> utils = new MessageBuilderUtils<>(List.of(suitableMessageBuilder));

        String actualMessage = utils.getMessage(event, null);

        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(suitableMessageBuilder).getInstance();
        verify(suitableMessageBuilder).buildMessage(event, null);
    }

    private static class TestEvent {
    }
}