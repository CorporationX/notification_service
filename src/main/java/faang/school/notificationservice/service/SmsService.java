package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.context.VonageConfig;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService implements NotificationService {
    // Задание
//    Создать Spring-компонент SmsService, который будет использоваться для отправки
//    SMS нотификаций пользователю на его номер телефона. Отправка SMS будет происходить через Vonage.
//    — это сервис телефонии с удобным и бесплатным пробным API. Нужно будет с ним интегрироваться.
//
//    Для этого нужно создать личный аккаунт на https://dashboard.nexmo.com/getting-started/sms
//    и разобраться с документацией интеграции на Java. Сконфигурировать всё необходимое согласно
//    документации в notification_service, а затем использовать созданные Vonage компоненты внутри нового SmsService.
//
//    SmsService должен реализовать интерфейс NotificationService, который уже предоставлен.
//    По сути, SmsService просто принимает сообщение и информацию о пользователе и отправляет
//    ему SMS через Vonage с переданным сообщением.
//
//    Критерии приема
//
//    1.Создана конфигурация Vonage в application.yaml на личный аккаунт.
//
//    2. Созданы все необходимые Spring-бины для работы с Vonage.
//
//    3.SMS действительно отправляется на тестовый номер (личный).
//
//    4. Написаны unit-тесты.


    private VonageConfig vonageConfig;

    @Autowired
    public SmsService(VonageConfig vonageConfig) {
        this.vonageConfig = vonageConfig;
    }
    @Override
    public void send(UserDto user, String message) {

    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return null;
    }

    public static void main(String[] args) {
        VonageClient vonageClient = VonageConfig.createVonageClient();
        TextMessage message = new TextMessage("Vonage APIs",
                "79168822014", "A text message sent using the Vonage SMS API");
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }
}
