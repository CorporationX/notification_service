package faang.school.notificationservice.service.telegram.command;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandName {
    START("/start"),
    ERROR("/error"),
    HELP("/help"),;

    private final String commandName;
}
