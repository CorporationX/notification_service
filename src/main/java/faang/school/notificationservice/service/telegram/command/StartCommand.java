package faang.school.notificationservice.service.telegram.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartCommand extends Command {
    @Value("${telegram.command.start-command}")
    private String startCommand;
    @Value("${telegram.command.start-message}")
    private String startMessage;

    @Override
    public SendMessage build(String text, long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(startMessage)
                .build();
    }

    @Override
    public boolean isApplicable(String textCommand) {
        return textCommand.equals(startCommand);
    }
}
