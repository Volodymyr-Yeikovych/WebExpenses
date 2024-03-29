package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.s28201.webExpenses.repository.ExpenseCategoryRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseShopRepository;
import pl.edu.s28201.webExpenses.service.SecurityService;

@Controller
@Slf4j
@RequestMapping("/edit")
public class DeleteCategoryShopController {

    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseShopRepository shopRepository;
    private final SecurityService securityService;

    public DeleteCategoryShopController(ExpenseCategoryRepository categoryRepository, ExpenseShopRepository shopRepository, SecurityService securityService) {
        this.categoryRepository = categoryRepository;
        this.shopRepository = shopRepository;
        this.securityService = securityService;
    }

    @GetMapping
    public String displayDeleteCategoryShopPage(Model model) {
        log.info("GET: Inside displayDeleteCategoryShopPage()");

        model.addAttribute("categories", categoryRepository.findByUser(securityService.getUserFromSecurity()));
        model.addAttribute("shops", shopRepository.findByUser(securityService.getUserFromSecurity()));

        return "deleteShopCategory";
    }

    @PostMapping
    public String returnExpensesPage(@RequestParam("selectedCategories") String categories, @RequestParam("selectedShops") String shops) {
        log.info("POST: Inside returnExpensesPage()");

        log.info("Categories to delete: [{}]", categories);
        log.info("Shops to delete: [{}]", shops);

        return "redirect:/expenses";
    }
}
