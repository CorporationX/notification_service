package faang.school.notificationservice.service;

import faang.school.notificationservice.config.provider.SmsRuProperties;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SmsNotificationServiceTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private SmsRuProperties properties = new SmsRuProperties("test-key", "https://sms.ru/sms/send");
    private SmsNotificationService service;

    @BeforeEach
    void setup() {
        service = new SmsNotificationService(webClient, properties);

        lenient().when(webClient.get())
                .thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);

        lenient().when(requestHeadersUriSpec.uri(anyString()))
                .thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
        lenient().when(requestHeadersUriSpec.uri(Mockito.<java.util.function.Function>any()))
                .thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);

        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        lenient().when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("OK"));
    }

    @Test
    void sendValidPhoneCallsWebClient() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("79991234567");

        service.send(user, "Test message");

        Mockito.verify(webClient, times(1)).get();
    }

    @Test
    void sendNullPhoneDoesNotCallWebClient() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone(null);

        service.send(user, "Test message");

        Mockito.verify(webClient, never()).get();
    }

    @Test
    void sendInvalidPhoneDoesNotCallWebClient() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("12345");

        service.send(user, "Test message");

        Mockito.verify(webClient, never()).get();
    }

    @Test
    void sendBlankPhoneDoesNotCallWebClient() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("   ");

        service.send(user, "Test message");

        Mockito.verify(webClient, never()).get();
    }

    @Test
    void getPreferredContact_ReturnsPhone() {
        assertEquals(UserDto.PreferredContact.PHONE, service.getPreferredContact());
    }
}