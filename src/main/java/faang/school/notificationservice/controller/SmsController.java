package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Этот контроллер создан только для тестирования корректности работы
 * @see SmsService и его метода
 * @see SmsService#send(UserDto, String) - отправляет уведомление СМС сообщением
 * Просто так не играться, баланс мог закончиться
 */

@RestController
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/sms/notification")
    public void send(@RequestBody UserDto userDto) {
        smsService.send(userDto, "Вам пришло тестовое уведомление!");
    }
}
