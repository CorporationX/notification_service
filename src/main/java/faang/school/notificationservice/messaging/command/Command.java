package faang.school.notificationservice.messaging.command;

public abstract class Command {
    public abstract SendMessage execute(CommandDto commandDto);

}
