package faang.school.notificationservice.service.notification.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.service.TelegramProfilesService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

@ExtendWith(MockitoExtension.class)
public abstract class CommandTest {

    @Mock
    protected MessageSource messageSource;
    @Mock
    protected TelegramProfilesService telegramProfilesService;
    @Mock
    protected UserServiceClient userServiceClient;

    protected Locale defaultLocale = new Locale("ru", "RU");


}
