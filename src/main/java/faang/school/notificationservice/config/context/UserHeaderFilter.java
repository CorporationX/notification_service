package faang.school.notificationservice.config.context;

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

    public static final String X_USER_ID_MISSING = "Missing required header 'x-user-id'. " +
            "Please include 'x-user-id' header with a valid user ID in your request.";
    private final UserContext userContext;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        String userId = req.getHeader("x-user-id");
        if (userId != null) {
            userContext.setUserId(Long.parseLong(userId));
        } else {
            throw new IllegalArgumentException(X_USER_ID_MISSING);
        }
        try {
            chain.doFilter(request, response);
        } finally {
            userContext.clear();
        }
    }
}
