package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/expenses")
@Slf4j
public class ExpensesController {

    @Autowired
    public ExpensesController() {

    }

    @GetMapping
    public String displayHelloPage() {
        log.info("GET: Inside displayHelloPage()");
        return "expenses";
    }
}
