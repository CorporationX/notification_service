package faang.school.notificationservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class SmsVonageAsyncConfig implements AsyncConfigurer {
    private static final int CORE_POOL_SIZE = 5;
    private static final int CORE_POOL_SIZE_MAX = 10;
    private static final int QUEUE_CAPACITY = 100;

    @Bean(name = "smsTaskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(CORE_POOL_SIZE_MAX);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("SmsExecutor");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) ->
                log.error("Uncaught exception in async method {} with params {}: {}",
                        method.getName(), params, throwable.getMessage());
    }

}
