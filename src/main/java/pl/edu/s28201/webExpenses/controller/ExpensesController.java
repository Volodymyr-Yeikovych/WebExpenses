package pl.edu.s28201.webExpenses.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.s28201.webExpenses.model.*;
import pl.edu.s28201.webExpenses.model.dto.ExpenseDto;
import pl.edu.s28201.webExpenses.model.expense.Expense;
import pl.edu.s28201.webExpenses.model.expense.ExpenseSortType;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;
import pl.edu.s28201.webExpenses.service.AppUserService;
import pl.edu.s28201.webExpenses.service.ExpenseService;
import pl.edu.s28201.webExpenses.service.SecurityService;

import java.util.*;

@Controller
@RequestMapping("/expenses")
@Slf4j
public class ExpensesController {

    private final ExpenseRepository expenseRepository;
    private final SecurityService securityService;
    private final ExpenseService expenseService;
    private final AppUserService userService;

    @Autowired
    public ExpensesController(ExpenseRepository expenseRepository,
                              SecurityService securityService, ExpenseService expenseService, AppUserService userService) {
        this.expenseRepository = expenseRepository;
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

        List<ExpenseDto> dtos = expenseService.parseToExpenseDto(sortAndRetrieveExpenses(sortType));

        model.addAttribute("expenses", dtos);

        String username = userService.getUsernameFromUser(securityService.getUserFromSecurity());
        model.addAttribute("username", username);

        return "expenses";
    }

    private List<Expense> sortAndRetrieveExpenses(String strSort) {
        ExpenseSortType sortType = expenseService.getSortTypeFromString(strSort);

        return getSortedExpenses(sortType);
    }

    private List<Expense> getSortedExpenses(ExpenseSortType sortType) {
        AppUser user = securityService.getUserFromSecurity();

        return switch (sortType) {
            case DATE_ASC -> expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.ASC, "timeMade"));
            default -> expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.DESC, "timeMade"));

            case CATEGORY_ABC_ASC -> expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.ASC, "category.name"));
            case CATEGORY_ABC_DESC -> expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.DESC, "category.name"));

            case SHOP_ABC_ASC -> expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.ASC, "shop.name"));
            case SHOP_ABC_DESC -> expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.DESC, "shop.name"));

            case MONEY_TO_USD_ASC -> expenseService.sortByMoneyAsc(expenseRepository.findExpensesByUser(user));
            case MONEY_TO_USD_DESC -> expenseService.sortByMoneyDesc(expenseRepository.findExpensesByUser(user));
        };
    }

    @PostMapping
    public String returnExpensesPage(@RequestParam(value = "selectedExpenses", defaultValue = "") String selectedExpensesIds) {
        log.info("POST: Inside returnExpensesPage()");
        if (selectedExpensesIds != null && !selectedExpensesIds.isEmpty()) {
            List<UUID> ids = expenseService.parseExpenseIds(selectedExpensesIds, ",");
            log.info("Parsed Expenses IDs to Delete: " + ids.toString());
            expenseRepository.deleteAllById(ids);
        } else {
            log.info("No expenses selected");
        }
        return "redirect:/expenses";
    }
}
