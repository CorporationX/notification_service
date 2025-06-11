package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class StartupNotifier {

    private final TelegramService telegramService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        UserDto user = new UserDto();
        user.setId(-1002709076416L);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
        String message = "Application 'NotificationServiceApp' started at " + formattedDateTime + "!";

        telegramService.send(user, message);
    }
}
