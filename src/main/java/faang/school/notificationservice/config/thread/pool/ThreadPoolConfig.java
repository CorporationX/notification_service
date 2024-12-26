package faang.school.notificationservice.config.thread.pool;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Data
public class ThreadPoolConfig {

    @Value("${thread-pool.sms-message-pool.num-of-threads}")
    private int smsMessagePoolNumOfThreads;

    public static final String DEFAULT_POOL_BEAN_NAME = "smsMessagePool";

    @Bean(DEFAULT_POOL_BEAN_NAME)
    public ThreadPoolTaskExecutor getSmsMessagePool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(smsMessagePoolNumOfThreads);
        return executor;
    }
}
