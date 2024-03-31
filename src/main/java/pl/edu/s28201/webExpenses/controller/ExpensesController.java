package pl.edu.s28201.webExpenses.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.s28201.webExpenses.model.dto.ExpenseDto;
import pl.edu.s28201.webExpenses.service.AppUserService;
import pl.edu.s28201.webExpenses.service.ExpenseService;
import pl.edu.s28201.webExpenses.service.SecurityService;

import java.util.*;

@Controller
@RequestMapping("/expenses")
@Slf4j
public class ExpensesController {
    private final SecurityService securityService;
    private final ExpenseService expenseService;
    private final AppUserService userService;

    @Autowired
    public ExpensesController(SecurityService securityService,
                              ExpenseService expenseService,
                              AppUserService userService) {
        this.securityService = securityService;
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping
    public String displayExpensePage(Model model, HttpSession session) {
        log.info("GET: Inside displayExpensePage()");
        Object objectType = session.getAttribute("sortType");
        String sortType = "date_desc";
        if (objectType != null) {
            sortType = (String) objectType;
        }
        model.addAttribute("sortType", sortType);

        List<ExpenseDto> dtos = expenseService.parseToExpenseDto(sortType);

        model.addAttribute("expenses", dtos);
        model.addAttribute("filtered", expenseService.isFiltered());

        String username = userService.getUsernameFromUser(securityService.getUserFromSecurity());
        model.addAttribute("username", username);

        return "expenses";
    }

    @PostMapping
    public String returnExpensesPage(@RequestParam(value = "selectedExpenses", defaultValue = "") String selectedExpensesIds) {
        log.info("POST: Inside returnExpensesPage()");

        expenseService.deleteAllById(selectedExpensesIds);

        return "redirect:/expenses";
    }
}
