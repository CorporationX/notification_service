package faang.school.notificationservice.service.sms_sending;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.notification_sending_strategy.SendingNotification;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsTwilioService implements SendingNotification {
    @Value("${Twilio.twilio_number}")
    private String TWILIO_NUMBER;
    @Value("${Twilio.user_name}")
    private String USER_NAME;
    @Value("${Twilio.account_sid}")
    private String ACCOUNT_SID;
    @Value("${Twilio.password}")
    private String PASSWORD;

    @PostConstruct
    public void init() {
        Twilio.init(USER_NAME, PASSWORD, ACCOUNT_SID);
    }

    public void sendSms(UserDto userDto, String messageText) {

    }

    @Override
    public void sending(String email, String title, String messageText) {
        Message message = Message.creator(new PhoneNumber(userDto.getPhone())
                , new PhoneNumber(TWILIO_NUMBER), messageText).create();
    }
}
