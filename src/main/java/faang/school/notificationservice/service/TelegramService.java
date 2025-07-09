package faang.school.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.TelegramConfig;
import faang.school.notificationservice.dto.RegisterTelegramDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ErrorResponse;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.service.telegram.TelegramBot;
import faang.school.notificationservice.service.telegram.TelegramLabel;
import faang.school.notificationservice.service.telegram.TelegramMessages;
import faang.school.notificationservice.util.Utils;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ConditionalOnBean(TelegramConfig.class)
@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    public static final String USER_BY_PHONE_NOT_FOUND = "Error searching for a client by phone number. phone: {}";
    public static final String TELEGRAM_NOT_FOUND = "Error searching for a Telegram chat by number. chatId: {}";

    private final TelegramBotsApi telegramBotsApi;
    private final TelegramBot telegramBot;
    private final UserServiceClient userServiceClient;
    private final TelegramMessages messages;
    private final ObjectMapper objectMapper;
    private final Utils utils;

    @PostConstruct
    public void setUp() throws TelegramApiException {
        telegramBotsApi.registerBot(telegramBot);
        telegramBot.setService(this);
        log.info("telegram bot register successful");
    }

    @Override
    public void send(UserDto user, String text) {
        log.info("Send telegram message to user.\nuser name is: {}, chatId: {}\ntext: {}",
            user.getUsername(), user.getChatId(), text);
        if (StringUtils.isBlank(text) || StringUtils.isBlank(user.getChatId())) {
            return;
        }
        if (user.hasChatId()) {
            telegramBot.sendMessage(user.getChatId(), text);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    public void registerUser(String chatId, String phone) {
        log.info("request to register a phone in the notification system via telegram. chatId: {}; phone: {}",
            chatId, phone);
        StringBuilder responseTelegramText = new StringBuilder();
        try {
            RegisterTelegramDto telegramDto = new RegisterTelegramDto(chatId, phone);
            userServiceClient.registerTelegramChatId(telegramDto);
            log.info("the user has registered his phone for telegram chat. {}", telegramDto);
            responseTelegramText.append(messages.get(TelegramLabel.REGISTRATION_SUCCESS));
        } catch (FeignException fe) {
            String errorMessage = utils.format(USER_BY_PHONE_NOT_FOUND, phone);
            responseTelegramText.append(messages.get(TelegramLabel.REGISTRATION_ERROR));
            handlerFeignException(fe, errorMessage);
        } finally {
            telegramBot.removeKeyboardAfterReceivingContact(chatId, responseTelegramText.toString());
        }
    }

    public void unregisterUser(String chatId) {
        log.info("request to cancel user registration in telegram. chatId: {}", chatId);
        StringBuilder responseTelegramText = new StringBuilder();
        try {
            userServiceClient.unregisterTelegramChatId(chatId);
            log.info("the user canceled the registration of the phone for telegram chat. chatId: {}", chatId);
            responseTelegramText.append(messages.get(TelegramLabel.CANCEL_REGISTRATION_SUCCESS));
        } catch (FeignException fe) {
            String errorMessage = utils.format(TELEGRAM_NOT_FOUND, chatId);
            responseTelegramText.append(messages.get(TelegramLabel.CANCEL_REGISTRATION_ERROR));
            handlerFeignException(fe, errorMessage);
        } finally {
            telegramBot.removeKeyboardAfterReceivingContact(chatId, responseTelegramText.toString());
        }
    }

    private void handlerFeignException(FeignException fe, String errorMessage) {
        log.error("FeignException.status is: [{}]", fe.status());
        log.error("feignException: {}", fe.getMessage(), fe);
        StringBuilder resultMessage = new StringBuilder();
        if (fe.status() == -1) {
            resultMessage.append(errorMessage);
        } else {
            String feignExceptionMessage = fe.contentUTF8();
            if (feignExceptionMessage != null) {
                try {
                    ErrorResponse errorResponse = objectMapper.readValue(
                        feignExceptionMessage, ErrorResponse.class);
                    resultMessage.append(errorResponse.getErrorMessage());
                } catch (JsonProcessingException e) {
                    log.error("JsonProcessingException: {}", e.getMessage(), e);
                    resultMessage.append(errorMessage);
                }
            } else {
                resultMessage.append(errorMessage);
            }
        }
        throw new UserNotFoundException(resultMessage.toString());
    }
}
