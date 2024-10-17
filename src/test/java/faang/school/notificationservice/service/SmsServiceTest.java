package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.impl.sms.SmsService;
import faang.school.notificationservice.validator.sms.SmsValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {
    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsValidator smsValidator;

    @Mock
    private SmsSubmissionResponse response;
    @Mock
    private SmsClient smsClient;

    @InjectMocks
    private SmsService smsService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().id(1L).build();
        userDto.setPreference(UserDto.PreferredContact.SMS);
    }

    @Test
    @DisplayName("Проверка на соответствие PreferredContact")
    void getPreferredContactGivenValidaPreferredReturnSMS() {
        //given
        userDto.setPreference(UserDto.PreferredContact.SMS);
        //when
        var result = smsService.getPreferredContact();
        //then
        assertEquals(userDto.getPreference(), result);
    }

    @Test
    @DisplayName("Проверка на несоответствие PreferredContact")
    void getPreferredContactGivenNotValidaPreferredReturnEmpty() {
        //given
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        //when
        var result = smsService.getPreferredContact();
        //given
        assertNotEquals(userDto.getPreference(), result);
    }

    @Test
    @DisplayName("Проверка vonageClient")
    void send() {
        //given
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        //when
        smsService.send(userDto, "Test text");

        //then
        verify(vonageClient.getSmsClient(), times(1)).submitMessage(any(TextMessage.class));
        verify(smsValidator, times(1)).validateResponse(response);
    }

}