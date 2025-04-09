package faang.school.notificationservice.config.context;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHeaderFilter implements Filter {

    private final UserContext userContext;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        String userId = req.getHeader("x-user-id");
        if (userId != null && !userId.isBlank()) {
            try {
                userContext.setUserId(Long.parseLong(userId));
            } catch (NumberFormatException e) {
                log.warn("Invalid x-user-id format: " + userId);
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            userContext.clear();
        }
    }
}
