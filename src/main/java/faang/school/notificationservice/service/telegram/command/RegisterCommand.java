package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterCommand extends Command {
    @Value("${telegram.command.register-command}")
    private String registerCommand;

    private final String example = "username:yourName password:YourPassword";
    //@Value("${telegram.command.start-message}")
    private String registrationMessage1 = "You are already registered";
    //@Value("${telegram.command.start-message}")
    private String registrationMessage2 = "Do what I said u to do to register!!!";
    private final TelegramAccountService telegramAccountService;

    @Override
    public SendMessage build(String text, long chatId) {
        String answer = telegramAccountService.existsByChatId(chatId)
                ? registrationMessage1
                : registrationMessage2;

        String username = parseUsername(text);
        String password = parsePassword(text);

        return SendMessage.builder()
                .chatId(chatId)
                .text(answer + " " + username + " " + password)
                .build();
    }

    @Override
    public boolean isApplicable(String commandText) {
        return commandText.contains(registerCommand);
    }

    private String parseUsername(String input) {
        return parse(input, "username:");
    }

    private String parsePassword(String input) {
        return parse(input, "password:");
    }

    private String parse(String input, String parsePattern) {
        String[] parts = input.split("\\s+");

        for (String part : parts) {
            if (part.startsWith(parsePattern)) {
                return part.substring(parsePattern.length());
            }
        }
        throw new RuntimeException("failed to process command = " + input);
    }
}