package faang.school.notificationservice.config.thread.pool;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Data
public class ThreadPoolConfig {

    @Value("${thread-pool.sms-message-pool.num-of-threads}")
    private int smsMessagePoolNumOfThreads;

    public static final String SMS_MESSAGE_POOL_BEAN_NAME = "smsMessagePool";

    @Bean(SMS_MESSAGE_POOL_BEAN_NAME)
    public ExecutorService getSmsMessagePool() {
        return Executors.newFixedThreadPool(smsMessagePoolNumOfThreads);
    }
}
