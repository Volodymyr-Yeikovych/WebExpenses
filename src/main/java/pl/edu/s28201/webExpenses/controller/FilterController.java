package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/expenses/filter")
public class FilterController {

    @GetMapping
    public String displayFilterPage() {
        log.info("GET: Inside displayFilterPage()");

        return "expenseFilter";
    }
}
