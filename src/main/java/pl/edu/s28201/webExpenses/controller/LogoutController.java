package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout")
@Slf4j
public class LogoutController {

    @GetMapping
    public String displayLogoutPage() {
        log.info("GET: Inside displayLogoutPage()");
        return "logout";
    }
}
