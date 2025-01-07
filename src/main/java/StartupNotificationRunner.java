import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupNotificationRunner implements CommandLineRunner {

    private final SmsService smsNotificationService;



    @Override
    public void run(String... args) {
        UserDto userDto = UserDto.builder()
                .phone("79997159242")
                .build();
        smsNotificationService.send(userDto,"Privet");
    }
}