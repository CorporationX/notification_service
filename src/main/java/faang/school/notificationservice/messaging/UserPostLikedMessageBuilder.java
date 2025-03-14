package faang.school.notificationservice.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserPostLikedMessageBuilder {
    private final MessageSource messageSource;

    public String getPredefinedMessage(Locale locale) {
        return messageSource.getMessage("notification.user.post.liked",
                new Object[]{followee, formattedDateTime},
                profileOwner.getLocale());
    }

    public String getPredefinedMessageForCurrentLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        return getPredefinedMessage(locale);
    }
}
