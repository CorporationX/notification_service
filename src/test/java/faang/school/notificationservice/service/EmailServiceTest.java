package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    private final List<String> emailsTo = List.of("dda20040609@gmail.com", "salikhdev@gmail.com", "vladerm2000@yandex.ru");

    @Test
    public void sendEmailTest() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        emailsTo.forEach(email ->
                futures.add(CompletableFuture.runAsync(() -> {
                    UserDto userDto = UserDto.builder()
                            .email(email)
                            .build();

                    String message = "Test, sorry, оно всегда теперь будет тебе посылаться, бро";
                    emailService.send(userDto, message);
                }))
        );

        futures.forEach(CompletableFuture::join);
    }
}