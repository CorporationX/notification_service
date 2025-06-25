package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class StartupNotifier {

    private static final String MESSAGE_TEMPLATE = "Application 'NotificationServiceApp' started at %s!";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss z";

    private final TelegramService telegramService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        UserDto user = createSystemUser();
        String message = createStartupMessage();

        telegramService.send(user, message);
    }

    private UserDto createSystemUser() {
        return UserDto.builder()
                .id(-1002709076416L)
                .build();
    }

    private String createStartupMessage() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        return String.format(MESSAGE_TEMPLATE, formattedDateTime);
    }
}