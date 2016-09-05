package fi.livi.digitraffic.meri.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fi.livi.digitraffic.meri.annotation.CoverageIgnore;

@Controller
@CoverageIgnore
public class HomeController {
    @RequestMapping("/")
    public String home() {
        return "redirect:swagger-ui.html";
    }
}
