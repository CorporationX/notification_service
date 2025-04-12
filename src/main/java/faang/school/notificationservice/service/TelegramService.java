package faang.school.notificationservice.service;

import faang.school.notificationservice.config.app.TelegramProperties;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService extends TelegramLongPollingBot implements NotificationService{

    private final TelegramProperties telegramProperties;

    @Override
    public void send(UserDto user, String message) {
        SendMessage msg = new SendMessage();
        msg.setChatId(user.getTelegramId()); // У нас в базе нету telegramId- вам нужно передать id в Json при запросе
        msg.setText(message);
        try{
            execute(msg);
            log.info("Сообщение успешно отправленно!");
        }catch (TelegramApiException e){
            log.error("Ошибка при отправке сообщение пользователю (Id = {}) {}",user.getId(), e.getMessage());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Ващ Id в Телеграмм: {}",update.getMessage().getChat().getId());
    }

    @Override
    public String getBotUsername() {
        return telegramProperties.getUsername();
    }
    @Override
    @Deprecated
    public String getBotToken(){
        return telegramProperties.getToken();
    }
}
