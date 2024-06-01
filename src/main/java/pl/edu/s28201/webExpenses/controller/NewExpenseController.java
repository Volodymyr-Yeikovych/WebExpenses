package pl.edu.s28201.webExpenses.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.expense.Expense;
import pl.edu.s28201.webExpenses.service.currency.CurrencyService;
import pl.edu.s28201.webExpenses.repository.ExpenseCategoryRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseShopRepository;
import pl.edu.s28201.webExpenses.service.SecurityService;

@RequestMapping("/expenses/new")
@Controller
@Slf4j
public class NewExpenseController {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseShopRepository shopRepository;
    private final CurrencyService currencyService;

    private final SecurityService securityService;

    @Autowired
    public NewExpenseController(
            ExpenseRepository expenseRepository,
            ExpenseCategoryRepository categoryRepository,
            ExpenseShopRepository shopRepository,
            CurrencyService currencyService,
            SecurityService securityService
    ) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.shopRepository = shopRepository;
        this.currencyService = currencyService;
        this.securityService = securityService;
    }

    @GetMapping
    public String displayNewExpensePage(Model model) {
        log.info("GET: /expenses/new");

        model.addAttribute("expense", new Expense());
        fillModel(model);

        return "newExpense";
    }

    private void fillModel(Model model) {
        AppUser currentUser = securityService.getUserFromSecurity();
        model.addAttribute("categories", categoryRepository.findAllByUserAndNotHidden(currentUser));
        model.addAttribute("shops", shopRepository.findAllByUserAndNotHidden(currentUser));
        model.addAttribute("currencies", currencyService.findAllCurrenciesCodeToNameMap());
    }

    @PostMapping
    public String returnReadyExpense(
            @ModelAttribute @Valid Expense expense,
            Errors errors,
            Model model
    ) {
        log.info("POST: /expenses/new");

        if (errors.hasErrors()) {
            fillModel(model);

            if (errors.hasFieldErrors("timeMade")) {
                model.addAttribute("dateErrorMsg", "Date can't be empty or in the future.");
            }

            if (errors.hasFieldErrors("moneySpent")) {
                model.addAttribute("moneyErrorMsg", "Money spent should be numerical value");
            }

            return "newExpense";
        }

        expense.setUser(securityService.getUserFromSecurity());
        expense.setMoneySpent(expense.getMoneySpent());
        expense.setCurrency(expense.getCurrency());
        expense.setShop(expense.getShop());
        expense.setCategory(expense.getCategory());
        expense.setDescription(expense.getDescription());

        expenseRepository.save(expense);

        return "redirect:/expenses";
    }

    @ModelAttribute("expense")
    public Expense expense() {
        return new Expense();
    }

}
