package assignment.java5.kudo_computer.config;

import assignment.java5.kudo_computer.entity.User;
import assignment.java5.kudo_computer.service.SessionService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class FilterConfig implements Filter {

    @Autowired
    private SessionService sessionService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        User user = sessionService.get("currentUser", null);

        // Danh sách các link cần đăng nhập
        boolean isCartAction = uri.startsWith(contextPath + "/cart");
        boolean isAdminAction = uri.startsWith(contextPath + "/admin");

        if (isAdminAction || isCartAction) {
            if (user == null || (isAdminAction && !user.isAdmin())) {

                // LẤY TRANG HIỆN TẠI (TRANG TRƯỚC ĐÓ)
                // Ví dụ: Đang ở /product/asus... mà bấm thêm vào giỏ,
                // thì Referer chính là trang /product/asus...
                String referer = req.getHeader("Referer");
                String redirectTarget = (referer != null) ? referer : contextPath + "/home";

                // Encode URL để truyền vào tham số redirect
                String encodedUrl = URLEncoder.encode(redirectTarget, StandardCharsets.UTF_8);

                res.sendRedirect(contextPath + "/login?redirect=" + encodedUrl);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}