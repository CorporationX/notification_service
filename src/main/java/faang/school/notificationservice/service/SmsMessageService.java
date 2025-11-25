package faang.school.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        // есть конечно морока с отправкой (из-за работы со сторонним сервисом)
        // поэтому тут моки
        String test = "send Misha";
        String numberTest = "89197282055";

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);

            String auth = email + ":" + apiKey;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            request.setHeader("Authorization", "Basic " + encodedAuth);
            request.setHeader("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("number", numberTest);
            jsonObject.put("text", message);
            jsonObject.put("sign", defaultSignature);
            jsonObject.put("channel", "DIRECT");

            StringEntity entity = new StringEntity(jsonObject.toString());
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
            throw new RuntimeException(e);
        }
    }
}
