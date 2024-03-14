package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.SecurityAppUser;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;

@Controller
@RequestMapping("/expenses")
@Slf4j
public class ExpensesController {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpensesController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping
    public String displayExpensePage(Model model) {
        log.info("GET: Inside displayExpensePage()");
        AppUser currentUser = getUserFromSecurity();
        model.addAttribute("expenses", expenseRepository.findExpensesByUser(currentUser));
        return "expenses";
    }

    private AppUser getUserFromSecurity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SecurityAppUser securityAppUser = (SecurityAppUser) authentication.getPrincipal();
        return securityAppUser.getUser();
    }
}
