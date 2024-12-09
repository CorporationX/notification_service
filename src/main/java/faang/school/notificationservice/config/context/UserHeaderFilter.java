package faang.school.notificationservice.config.context;

import cn.hutool.core.text.AntPathMatcher;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserHeaderFilter implements Filter {

    private final UserContext userContext;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> EXCLUDED_PATTERNS = Arrays.asList(
            "/api/v1/webhooks/vonage/**",
            "/health",
            "/actuator/**"
    );


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (isExcludedPath(req.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        String userId = req.getHeader("x-user-id");
        if (userId != null) {
            userContext.setUserId(Long.parseLong(userId));
        }else {
            throw new IllegalArgumentException("Missing required header 'x-user-id'. Please include 'x-user-id' header with a valid user ID in your request.");
        }
        try {
            chain.doFilter(request, response);
        } finally {
            userContext.clear();
        }
    }

    private boolean isExcludedPath(String requestUri) {
        return EXCLUDED_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }
}
