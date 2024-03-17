package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.s28201.webExpenses.model.Expense;

@Controller
@RequestMapping("/expenses/new")
@Slf4j
public class AddExpenseController {

    @GetMapping
    public String displayNewExpensePage(Model model) {
        log.info("GET: Inside displayNewExpensePage()");
        model.addAttribute("expense", new Expense());
        return "newExpense";
    }

    @PostMapping
    public String returnReadyExpense(@ModelAttribute("expense") Expense expense) {
        log.info("POST: Inside returnReadyExpense()");

        return "redirect:/expenses";
    }
}
