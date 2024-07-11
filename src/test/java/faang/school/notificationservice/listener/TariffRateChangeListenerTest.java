package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.TariffRateChangeEvent;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.TariffRateChangeMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TariffRateChangeListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TariffRateChangeMessageBuilder tariffRateChangeMessageBuilder;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TariffRateChangeListener tariffRateChangeListener;

    private TariffRateChangeEvent tariffRateChangeEvent;
    private UserNotificationDto userNotificationDto;
    private Message message;
    private Class tClass;
    private  byte[] body;


    @BeforeEach
    void setUp() {
        tariffRateChangeListener = new TariffRateChangeListener(objectMapper, userServiceClient,
                List.of(tariffRateChangeMessageBuilder), List.of(notificationService));
        tariffRateChangeEvent = TariffRateChangeEvent.builder()
                .savingsAccountId(1L)
                .tariffId(2L)
                .newRate(BigDecimal.valueOf(0.05))
                .changeDate(LocalDateTime.now().plusDays(7))
                .ownerId(3L)
                .build();

        userNotificationDto = UserNotificationDto.builder()
                .username("Max")
                .build();
        tClass = tariffRateChangeEvent.getClass();
        message = mock(Message.class);
        body = new byte[0];
    }

    @Test
    public void whenOnMessageSuccessfully() throws IOException {
        byte[] body = new byte[0];
        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, TariffRateChangeEvent.class)).thenReturn(tariffRateChangeEvent);
        when(tariffRateChangeMessageBuilder.getInstance()).thenReturn(tClass);
        when(tariffRateChangeMessageBuilder.buildMessage(any(), any(), any())).thenReturn("Text");
        when(userServiceClient.getDtoForNotification(anyLong())).thenReturn(userNotificationDto);
        tariffRateChangeListener.onMessage(message, body);
        verify(objectMapper).readValue(message.getBody(), TariffRateChangeEvent.class);
    }
}