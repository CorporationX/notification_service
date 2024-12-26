package faang.school.notificationservice.service.jpa;

import faang.school.notificationservice.config.thread.pool.ThreadPoolConfig;
import faang.school.notificationservice.exceptions.ResourceNotFoundException;
import faang.school.notificationservice.model.SmsMessage;
import faang.school.notificationservice.repository.SmsMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsMessageService {

    private final SmsMessageRepository smsMessageRepository;

    @Async(value = ThreadPoolConfig.DEFAULT_POOL_BEAN_NAME)
    public CompletableFuture<SmsMessage> saveSmsMessageAsync(SmsMessage smsMessage) {
        SmsMessage savedMsg = smsMessageRepository.save(smsMessage);
        return CompletableFuture.completedFuture(savedMsg);
    }

    public SmsMessage getSmsMessageByUid(Long uid) {
        return smsMessageRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("SmsMessage", "uid", uid));
    }
}
