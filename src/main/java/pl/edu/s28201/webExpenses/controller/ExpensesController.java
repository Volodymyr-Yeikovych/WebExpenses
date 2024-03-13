package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;

@Controller
@RequestMapping("/expenses")
@Slf4j
public class ExpensesController {

    private ExpenseRepository expenseRepository;

    @Autowired
    public ExpensesController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping
    public String displayHelloPage(Model model) {
        log.info("GET: Inside displayHelloPage()");
        model.addAttribute("expenses", expenseRepository.findAll());
        return "expenses";
    }
}
