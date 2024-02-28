package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.Message;

import static org.mockito.Mockito.verify;

public class AbstractEventListenerTest<T> {
    private Message message;
    @Test
    private void test() {
        AbstractEventListener<T> listener = Mockito.mock(AbstractEventListener.class, Answers.CALLS_REAL_METHODS);

        verify(listener.getEvent(message, (Class<T>) UserDto.class));

    }
}
