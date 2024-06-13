package faang.school.notificationservice.service.telegram.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandBuilder {

    public SendMessage buildMessage(long chatId, String message) {

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();

        log.info("Created message {} for chatId {}", message, chatId);

        return sendMessage;
    }
}
