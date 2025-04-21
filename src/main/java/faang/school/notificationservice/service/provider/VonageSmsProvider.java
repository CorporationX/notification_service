package faang.school.notificationservice.service.provider;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.SmsResponse;
import faang.school.notificationservice.exception.SmsIntegrationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VonageSmsProvider implements SmsProvider {
    private final VonageClient vonageClient;

    @Override
    public SmsResponse sendSms(String from, String to, String message) {
        TextMessage textMessage = new TextMessage(from, to, message);

        try {
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
            return convertResponse(response);
        } catch (VonageClientException e) {
            throw new SmsIntegrationException("Vonage error: " + e.getMessage(), e);
        }
    }

    private SmsResponse convertResponse(SmsSubmissionResponse vonageResponse) {
        SmsResponse smsResponse = new SmsResponse();

        if (vonageResponse.getMessages().isEmpty()) {
            smsResponse.setSuccess(false);
            smsResponse.setErrorMessage("Empty response from Vonage");
            return smsResponse;
        }

        SmsSubmissionResponseMessage message = vonageResponse.getMessages().get(0);
        smsResponse.setSuccess(message.getStatus() == MessageStatus.OK);
        smsResponse.setErrorMessage(message.getErrorText());
        smsResponse.setProviderId(message.getId());

        return smsResponse;
    }
}
