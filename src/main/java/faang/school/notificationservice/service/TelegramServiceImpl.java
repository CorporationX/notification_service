package faang.school.notificationservice.service;

import faang.school.notificationservice.NotificationTelegramBot;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements NotificationService{
private final NotificationTelegramBot notificationTelegramBot;
private final UserServiceClient userServiceClient;
    @Override
    public void send(UserDto user, String message) {
        SendMessage notificationMessage = new SendMessage();
//        notificationMessage.setChatId(userServiceClient.getUser();
//        прикрутить эндпойнт и брать айди юзера через кнопку или как

        notificationMessage.setText(message);

        try {
            notificationTelegramBot.execute(notificationMessage);
        } catch (TelegramApiException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
