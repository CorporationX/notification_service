package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
public class LikeEventMessageBuilderTest {

    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;
}
