package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.service.telegram.command.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommandProcessor {
    private static final String exceptionMessage = "command %s not found in commands base";
    private final List<Command> commands;

    public SendMessage buildSendMessage(String textCommand, long chatId) {
        return commands.stream().filter(command -> command.isApplicable(textCommand))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException(
                        String.format(exceptionMessage, textCommand))
                )
                .process(chatId, textCommand);
    }
}
