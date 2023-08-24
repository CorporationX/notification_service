package faang.school.notificationservice.builder;

import faang.school.notificationservice.dto.ProfileViewEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static reactor.core.publisher.Mono.when;

@ExtendWith(value = {MockitoExtension.class})
public class ProfileViewMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Test
    public void testBuildMessage() {
        ProfileViewEvent event = ProfileViewEvent.builder()
                .userId(1L)
                .profileViewedId(2L)
                .date(LocalDateTime.now())
                .build();
//        when(messageSource.getMessage(eq("profile_view.new"), any(), eq(Loca)))
    }

}
