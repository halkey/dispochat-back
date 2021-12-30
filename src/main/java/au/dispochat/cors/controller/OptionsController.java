package au.dispochat.cors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
public class OptionsController {
    @RequestMapping(value = "/*", method = RequestMethod.OPTIONS)
    public void corsHeaders(HttpServletResponse response) {
        System.out.println("ahey");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
        response.addHeader("Access-Control-Max-Age", "3600");
        response.setStatus(200);
    }
}
