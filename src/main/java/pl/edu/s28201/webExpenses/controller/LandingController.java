package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/", "/landing"})
@Slf4j
public class LandingController {

    @GetMapping
    public String displayLandingPage() {
        log.info("GET: / or /landing");

        return "landing";
    }
}
