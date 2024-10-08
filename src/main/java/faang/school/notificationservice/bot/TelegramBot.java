package faang.school.notificationservice.bot;

import faang.school.notificationservice.model.entity.TelegramUser;
import faang.school.notificationservice.repository.TelegramUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final TelegramUserRepository telegramUserRepository;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message updateMessage = update.getMessage();
            Chat chat = updateMessage.getChat();
            if (chat.isGroupChat()) {
                log.warn("Attempt to use bot from group chat, chat id = {}", chat.getId());
                sendGroupChatWarning(updateMessage.getChatId().toString());
                return;
            }

            User messageFrom = updateMessage.getFrom();
            String telegramUserId = messageFrom.getId().toString();
            String userName = messageFrom.getUserName();
            String firstName = messageFrom.getFirstName();
            String lastName = messageFrom.getLastName();

            Optional<TelegramUser> existingUser = telegramUserRepository.findById(Long.parseLong(telegramUserId));

            if (existingUser.isPresent()) {
                updateUserIfChanged(existingUser.get(), userName, firstName, lastName);
            } else {
                createNewTelegramUser(telegramUserId, userName, firstName, lastName);
            }
        }
    }

    private void sendGroupChatWarning(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("This bot doesn't work with group chats");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException was occurred while send warn to group chat", e);
        }
    }

    private void updateUserIfChanged(TelegramUser user, String userName, String firstName, String lastName) {
        boolean updated = false;

        if (userName != null && !userName.equals(user.getUserName())) {
            user.setUserName(userName);
            updated = true;
        }
        if (firstName != null && !firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
            updated = true;
        }
        if (lastName != null && !lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
            updated = true;
        }

        if (updated) {
            telegramUserRepository.save(user);
        }
    }

    private void createNewTelegramUser(String telegramUserId, String userName, String firstName, String lastName) {
        TelegramUser newUser = new TelegramUser();
        newUser.setTelegramUserId(Long.parseLong(telegramUserId));
        newUser.setUserName(userName);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        telegramUserRepository.save(newUser);
    }
}

