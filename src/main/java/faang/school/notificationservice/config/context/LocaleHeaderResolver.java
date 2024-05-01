package faang.school.notificationservice.config.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocaleHeaderResolver extends AcceptHeaderLocaleResolver {

    private final LocaleContextHolder localeContextHolder;

    @Override
    public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
        localeContextHolder.setLocale(resolveLocale(request));
    }
}
