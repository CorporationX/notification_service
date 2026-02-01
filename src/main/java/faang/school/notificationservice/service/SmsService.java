package faang.school.notificationservice.service;

import faang.school.notificationservice.SmsApi.SmsSender;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService{
    private final SmsSender smsSender;
    @Value("${mainsms.api.sender}")
    private String sender;
    @Override
    public void send(UserDto user, String message) {
        try {
            JSONObject resultJson = smsSender.MessageSend(message, user.getPhone(), sender);
            if (resultJson.get("status").equals("success"))
            {
                System.out.println("Сообщение успешно отправлено, стоимость отправки: "+resultJson.get("price")+" рублей");
            }
            else
            {
                System.out.println("Произошла ошибка: "+resultJson.get("message"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
