package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.dto.CommandDto;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static faang.school.notificationservice.service.telegram.command.CommandName.*;



@Component(value = "/help")
public class HelpCommand extends Command {
    private static final String HELP_MESSAGE = String.format("✨Доступные команды✨\n\n"
                    + "%s - начать работу со мной\n"
                    + "%s - получить ошибку, пока тестовая команда\n"
                    + "%s - получить помощь в работе со мной\n",
            START.getCommandName(), ERROR.getCommandName(), HELP.getCommandName());

    public HelpCommand(Environment environment) {
        super(environment);
    }

    @Override
    public SendMessage execute(CommandDto commandDto) {
        SendMessage sendMessage = makeMessage(commandDto.getText(), commandDto.getChatId());
        return sendMessage;
    }

    private SendMessage makeMessage(String messageText, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(String.format(HELP_MESSAGE, messageText));
        return sendMessage;
    }
}
