package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.sms.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тестирование {@link SmsService}
 *
 * @author Linempy
 * @since 15.08.2025
 */
@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @Mock
    private WebClient webClient;

    @InjectMocks
    private SmsService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "login", "test");
        ReflectionTestUtils.setField(service, "password", "test");
        ReflectionTestUtils.setField(service, "sender", "TEST");
        ReflectionTestUtils.setField(service, "api", "http://test.api");
    }

    @Test
    public void getPreferenceContactIsPhone() {
        UserDto.PreferredContact expectedPreferences = UserDto.PreferredContact.PHONE;

        UserDto.PreferredContact realPreferences = service.getPreferredContact();

        assertEquals(expectedPreferences, realPreferences);
    }
}