package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.s28201.webExpenses.model.expense.ExpenseCategory;
import pl.edu.s28201.webExpenses.repository.ExpenseCategoryRepository;
import pl.edu.s28201.webExpenses.service.SecurityService;

@Controller
@Slf4j
@RequestMapping("/category")
public class NewCategoryController {

    private final SecurityService securityService;
    private final ExpenseCategoryRepository categoryRepository;

    @Autowired
    public NewCategoryController(SecurityService securityService, ExpenseCategoryRepository categoryRepository) {
        this.securityService = securityService;
        this.categoryRepository = categoryRepository;
    }


    @GetMapping
    public String displayNewCategory(Model model) {
        log.info("GET: Inside displayNewCategory()");
        model.addAttribute("expenseCategory", new ExpenseCategory());
        return "newCategory";
    }

    @PostMapping
    public String returnReadyCategory(@ModelAttribute ExpenseCategory category,
                                      @RequestParam("name") String name) {
        log.info("POST: Inside returnReadyCategory()");

        category.setName(name);
        category.setUser(securityService.getUserFromSecurity());

        categoryRepository.save(category);

        return "redirect:/expenses";
    }

    @ModelAttribute("expenseCategory")
    public ExpenseCategory expense() {
        return new ExpenseCategory();
    }

}
