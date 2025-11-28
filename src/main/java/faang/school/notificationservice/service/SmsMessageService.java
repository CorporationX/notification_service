package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsSendMessageError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsMessageService implements NotificationService{

    private final static String TEST_MESSAGE = "send test";
    private final static String MOCK_NUMBER = "89000000000";

    @Value("${sms.aero.email}")
    private String email;
    @Value("${sms.aero.api-key}")
    private String apiKey;
    @Value("${sms.aero.signature}")
    private String defaultSignature;
    @Value("${sms.aero.base-url}")
    private String baseUrl;

    @Override
    public void send(UserDto userDto, String message) {
        sendMessage(message, userDto.getPhone());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

    public void sendMessage(String message, String number) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost request = buildHttpPost();

            JSONObject jsonObject = buildJSONObject();

            StringEntity entity = new StringEntity(jsonObject.toString(), StandardCharsets.UTF_8);
            request.setEntity(entity);

            HttpResponse httpResponse = client.execute(request);
            String response = EntityUtils.toString(httpResponse.getEntity());

            checkingServerResponse(httpResponse, response, message);

        } catch (Exception e) {
            throw new SmsSendMessageError("Error sending message to API SMS Aero", e);
        }
    }

    private void checkingServerResponse(HttpResponse httpResponse, String response, String message) {
        Integer statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
            log.info("Text {} send on user telephone for sms. Status code {} ", message, statusCode);
        } else {
            String mesError = new StringBuilder("Status - ")
                    .append(httpResponse.getStatusLine().getStatusCode())
                    .append(". Response-")
                    .append(response)
                    .toString();
            log.error("The notification was not sent due to an error - {} ", mesError);
        }
    }

    private JSONObject buildJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("number", MOCK_NUMBER);
        jsonObject.put("text", TEST_MESSAGE);
        jsonObject.put("sign", defaultSignature);
        jsonObject.put("channel", "DIRECT");
        return jsonObject;
    }

    private HttpPost buildHttpPost() {
        HttpPost request = new HttpPost(baseUrl);

        String auth = email + ":" + apiKey;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        request.setHeader("Authorization", "Basic " + encodedAuth);
        request.setHeader("Content-Type", "application/json; charset=UTF-8");

        return request;
    }

}
