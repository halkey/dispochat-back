package au.dispochat.cors.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class OptionsController {
    @Component
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Profile({"default", "infra-dev"})
    public class SimpleCorsFilter implements Filter {

    /*
        Swagger Password Grant Type CORS Error. Log Message:
        Access to fetch at 'http://localhost:8080/oauth/token' from origin 'http://localhost:8085'
        has been blocked by CORS policy: Response to preflight request doesn't pass access control check:
        It does not have HTTP ok status.
    */

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
            HttpServletResponse response = (HttpServletResponse) res;
            HttpServletRequest request = (HttpServletRequest) req;

            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "*");

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                chain.doFilter(req, res);
            }
        }

    }
}
