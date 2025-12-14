package faang.school.notificationservice.config.executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfig {
    @Value(value = "${thread.min-thread}")
    private int minThread;
    @Value(value = "${thread.max-thread}")
    private int maxThread;
    @Value(value = "${thread.queue-capacity}")
    private int queueCapacity;
    @Value("${thread.thread-name}")
    private String threadName;
    @Value("${thread.ttl-thread}")
    private int ttlThread;
    @Value(value = "${thread.graceful-shutdown-flag}")
    private boolean gracefulShutdownFlag;
    @Value("${thread.await-termination-seconds}")
    private int awaitTerminationSeconds;


    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(minThread);
        executor.setMaxPoolSize(maxThread);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadName);

        executor.setKeepAliveSeconds(ttlThread);
        executor.setWaitForTasksToCompleteOnShutdown(gracefulShutdownFlag);
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);

        executor.initialize();

        return executor;
    }
}