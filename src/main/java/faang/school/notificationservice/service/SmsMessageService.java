package faang.school.notificationservice.service;

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
public class SmsMessageService {

    @Value("${sms.aero.email}")
    private String email;
    @Value("${sms.aero.api-key}")
    private String apiKey;
    @Value("${sms.aero.signature}")
    private String defaultSignature;
    @Value("${sms.aero.base-url}")
    private String baseUrl;

    public String sendMessage(String message) {
        //todo просто левый номер для отправки
        // тут просто моки, ибо колабиться полноценно проблема
        // но для демо норм я думаю
        // полноценное сообщение отправлять тоже не получится, ибо для теста только send test принимают
        String test = "send test";
        String numberTest = "чей-то номер";

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);

            String auth = email + ":" + apiKey;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            request.setHeader("Authorization", "Basic " + encodedAuth);
            request.setHeader("Content-Type", "application/json; charset=UTF-8");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("number", numberTest);
            jsonObject.put("text", test);
            jsonObject.put("sign", defaultSignature);
            jsonObject.put("channel", "DIRECT");

            StringEntity entity = new StringEntity(jsonObject.toString(), StandardCharsets.UTF_8 );
            request.setEntity(entity);

            HttpResponse httpResponse = client.execute(request);
            String response = EntityUtils.toString(httpResponse.getEntity());
            log.info("Text {} send on user telephone for sms ", message);
            return  new StringBuilder("Status - ")
                    .append(httpResponse.getStatusLine().getStatusCode())
                    .append(". Response-")
                    .append(response)
                    .toString();
        } catch (Exception e) {
            throw new SmsSendMessageError("Error sending message to API SMS Aero", e);
        }
    }
}
