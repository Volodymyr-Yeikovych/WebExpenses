package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.expense.Expense;
import pl.edu.s28201.webExpenses.model.expense.ExpenseCategory;
import pl.edu.s28201.webExpenses.model.expense.ExpenseShop;
import pl.edu.s28201.webExpenses.repository.CurrencyRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseCategoryRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseShopRepository;
import pl.edu.s28201.webExpenses.service.SecurityService;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

@RequestMapping("/expenses/new")
@Controller
@Slf4j
public class NewExpenseController {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseShopRepository shopRepository;
    private final CurrencyRepository currencyRepository;

    private final SecurityService securityService;

    @Autowired
    public NewExpenseController(ExpenseRepository expenseRepository, ExpenseCategoryRepository categoryRepository, ExpenseShopRepository shopRepository, CurrencyRepository currencyRepository, SecurityService securityService) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.shopRepository = shopRepository;
        this.currencyRepository = currencyRepository;
        this.securityService = securityService;
    }

    @GetMapping
    public String displayNewExpensePage(Model model) {
        log.info("GET: Inside displayNewExpensePage()");
        AppUser currentUser = securityService.getUserFromSecurity();
        model.addAttribute("expense", new Expense());
        model.addAttribute("categories", categoryRepository.findByUser(currentUser));
        model.addAttribute("shops", shopRepository.findByUser(currentUser));
        model.addAttribute("currencies", currencyRepository.findAllCurrenciesCodeToNameMap());
        return "newExpense";
    }

    @PostMapping
    public String returnReadyExpense(@ModelAttribute Expense expense,
                                     @RequestParam("moneySpent") String moneySpent,
                                     @RequestParam("currency") String currency,
                                     @RequestParam("category") String categoryName,
                                     @RequestParam("shop") String shopName,
                                     @RequestParam(value = "description", required = false) String description) {
        log.info("POST: Inside returnReadyExpense()");

        Optional<ExpenseCategory> category = categoryRepository.findByName(categoryName);
        Optional<ExpenseShop> shops =  shopRepository.findByName(shopName);
        BigDecimal parsedMoneySpent = new BigDecimal(moneySpent);
        Currency parsedCurrency = Currency.getInstance(currency);

        expense.setUser(securityService.getUserFromSecurity());
        expense.setMoneySpent(parsedMoneySpent);
        expense.setCurrency(parsedCurrency);
        category.ifPresent(expense::setCategory);
        shops.ifPresent(expense::setShop);
        expense.setDescription(description);

        expenseRepository.save(expense);

        return "redirect:/expenses";
    }

    @ModelAttribute("expense")
    public Expense expense() {
        return new Expense();
    }
}
