package faang.school.notificationservice.config.context;

import faang.school.notificationservice.error.InvalidUserHeaderException;
import faang.school.notificationservice.error.MissingUserHeaderException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserHeaderFilter implements Filter {

    private final UserContext userContext;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        String userId = req.getHeader("x-user-id");
        try {
            if (userId == null || userId.isBlank()) {
                throw new MissingUserHeaderException("Missing required header 'x-user-id'");
            }
            try {
                long parsed = Long.parseLong(userId);
                userContext.setUserId(parsed);
            } catch (NumberFormatException nfe) {
                throw new InvalidUserHeaderException("Header 'x-user-id' must be a valid long");
            }
            chain.doFilter(request, response);
        } finally {
            userContext.clear();
        }
    }
}