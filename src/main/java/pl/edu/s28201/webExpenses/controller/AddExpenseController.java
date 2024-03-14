package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/expenses/new")
@Slf4j
public class AddExpenseController {

    @GetMapping
    public String displayNewExpensePage() {
        log.info("GET: Inside displayNewExpensePage()");
        return "newExpense";
    }

    @PostMapping
    public String returnReadyExpense() {
        log.info("POST: Inside returnReadyExpense()");
        return "redirect:/expenses";
    }
}
