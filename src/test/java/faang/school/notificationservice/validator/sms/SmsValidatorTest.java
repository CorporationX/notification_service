package faang.school.notificationservice.validator.sms;

import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import faang.school.notificationservice.exception.SmsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SmsValidatorTest {

    @InjectMocks
    private SmsValidator smsValidator;

    @Mock
    private SmsSubmissionResponse response;

    @Mock
    private SmsSubmissionResponseMessage responseMessage;


    @Test
    void validateResponseGivenSuccessfulResponse() {
        //given
        when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);
        when(response.getMessages()).thenReturn(List.of(responseMessage));

        //then
        assertDoesNotThrow(() -> smsValidator.validateResponse(response));
        verify(responseMessage, times(1)).getStatus();
    }

    @Test
    void validateResponseGivenFailureResponseReturnException() {
        //given
        when(responseMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);
        when(response.getMessages()).thenReturn(List.of(responseMessage));

        //then
        assertThrows(SmsException.class, () -> smsValidator.validateResponse(response));

    }
}