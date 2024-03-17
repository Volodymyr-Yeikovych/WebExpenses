package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.s28201.webExpenses.model.*;
import pl.edu.s28201.webExpenses.repository.CurrencyRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseCategoryRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseOriginRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Currency;
import java.util.Optional;

@Controller
@RequestMapping("/expenses")
@Slf4j
public class ExpensesController {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseOriginRepository originRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public ExpensesController(ExpenseRepository expenseRepository, ExpenseCategoryRepository categoryRepository, ExpenseOriginRepository originRepository, CurrencyRepository currencyRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.originRepository = originRepository;
        this.currencyRepository = currencyRepository;
    }

    @GetMapping
    public String displayExpensePage(Model model) {
        log.info("GET: Inside displayExpensePage()");
        AppUser currentUser = getUserFromSecurity();
        model.addAttribute("expenses", expenseRepository.findExpensesByUser(currentUser));
        model.addAttribute("expenseCategory", new ExpenseCategory());
        return "expenses";
    }

    private AppUser getUserFromSecurity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SecurityAppUser securityAppUser = (SecurityAppUser) authentication.getPrincipal();
        return securityAppUser.getUser();
    }

    @GetMapping("/new")
    public String displayNewExpensePage(Model model) {
        log.info("GET: Inside displayNewExpensePage()");
        AppUser currentUser = getUserFromSecurity();
        model.addAttribute("expense", new Expense());
        model.addAttribute("categories", categoryRepository.findByUser(currentUser));
        model.addAttribute("origins", originRepository.findByUser(currentUser));
        model.addAttribute("currencies", currencyRepository.findAllCurrenciesCodeToNameMap());
        return "newExpense";
    }

    @PostMapping("/new")
    public String returnReadyExpense(@ModelAttribute Expense expense,
                                     @RequestParam("moneySpent") String moneySpent,
                                     @RequestParam("currency") String currency,
                                     @RequestParam("category") String categoryName,
                                     @ModelAttribute("originObj") ExpenseOrigin expenseOrigin,
                                     @RequestParam("origin") String originName,
                                     @RequestParam(value = "description", required = false) String description) {
        log.info("POST: Inside returnReadyExpense()");

        Optional<ExpenseCategory> category = categoryRepository.findByName(categoryName);
        Optional<ExpenseOrigin> origin =  originRepository.findByName(originName);
        BigDecimal parsedMoneySpent = new BigDecimal(moneySpent);
        Currency parsedCurrency = Currency.getInstance(currency);

        expense.setUser(getUserFromSecurity());
        expense.setMoneySpent(parsedMoneySpent);
        expense.setCurrency(parsedCurrency);
        category.ifPresent(expense::setCategory);
        origin.ifPresent(expense::setOrigin);
        expense.setDescription(description);

        expenseRepository.save(expense);

        return "redirect:/expenses";
    }

    @PostMapping("/new-cat")
    public String returnReadyCategory(@ModelAttribute("expenseCategory") ExpenseCategory expense) {
        log.info("POST: Inside returnReadyCategory()");
        return "redirect:/expenses";
    }

    @ModelAttribute("expense")
    public Expense expense() {
        return new Expense();
    }
}
