package faang.school.notificationservice.threadpool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolForTelegramSend {

    @Value("${threadPool.nThreads.telegram}")
    private int nTreads;

    @Bean
    public ExecutorService telegramBotPool() {
        return Executors.newFixedThreadPool(nTreads);
    }
}
