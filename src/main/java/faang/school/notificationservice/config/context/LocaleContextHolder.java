package faang.school.notificationservice.config.context;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleContextHolder {

    private final ThreadLocal<Locale> userLocaleHolder = new ThreadLocal<>();

    public void setLocale(Locale locale) {
        userLocaleHolder.set(locale);
    }

    public Locale getLocale() {
        return userLocaleHolder.get() != null ? userLocaleHolder.get() : Locale.getDefault();
    }

    public void clear() {
        userLocaleHolder.remove();
    }
}
