package faang.school.notificationservice.service.notification.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.entity.TelegramAccount;
import faang.school.notificationservice.service.notification.SmsNotificationService;
import faang.school.notificationservice.service.notification.telegram.TelegramAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class ConfirmationCommand extends Command {
    private final TelegramAccountService telegramAccountService;
    private final SmsNotificationService smsNotificationService;
    private final UserServiceClient userServiceClient;
    @Value("${telegram.command.confirm}")
    private String confirmCommand;

    public ConfirmationCommand(MessageSource messageSource,
                               TelegramAccountService telegramAccountService,
                               SmsNotificationService smsNotificationService,
                               UserServiceClient userServiceClient) {
        super(messageSource);
        this.telegramAccountService = telegramAccountService;
        this.smsNotificationService = smsNotificationService;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public SendMessage process(long chatId, String text) {

        sendConfirmationLink(telegramAccountService.getByChatId(chatId));

        return buildSendMessage(chatId, getMessage("telegram.confirm.sent", null));
    }

    @Override
    public boolean isApplicable(String textCommand) {
        return textCommand.equals(confirmCommand);
    }

    private void sendConfirmationLink(TelegramAccount telegramAccount) {
        String code = telegramAccount.getId().toString();
        String confirmationMessage = String.format(
                getMessage("telegram.confirm.link", new Object[]{code}),
                telegramAccount.getId().toString()
        );

        smsNotificationService.send(userServiceClient.getUser(telegramAccount.getUserId()), confirmationMessage);
    }
}
