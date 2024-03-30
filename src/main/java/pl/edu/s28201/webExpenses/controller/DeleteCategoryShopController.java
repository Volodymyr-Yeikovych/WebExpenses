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
import pl.edu.s28201.webExpenses.service.UuidService;

import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/edit")
public class DeleteCategoryShopController {

    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseShopRepository shopRepository;
    private final SecurityService securityService;
    private final UuidService uuidService;

    public DeleteCategoryShopController(ExpenseCategoryRepository categoryRepository, ExpenseShopRepository shopRepository, SecurityService securityService, UuidService uuidService) {
        this.categoryRepository = categoryRepository;
        this.shopRepository = shopRepository;
        this.securityService = securityService;
        this.uuidService = uuidService;
    }

    @GetMapping
    public String displayDeleteCategoryShopPage(Model model) {
        log.info("GET: Inside displayDeleteCategoryShopPage()");

        model.addAttribute("categories", categoryRepository.findAllByUserAndNotHidden(securityService.getUserFromSecurity()));
        model.addAttribute("shops", shopRepository.findAllByUserAndNotHidden(securityService.getUserFromSecurity()));

        return "deleteShopCategory";
    }

    @PostMapping
    public String returnExpensesPage(@RequestParam(value = "selectedCategories", defaultValue = "") String categories,
                                     @RequestParam(value = "selectedShops", defaultValue = "") String shops) {
        log.info("POST: Inside returnExpensesPage()");

        if (categories != null && !categories.isEmpty()) {
            List<UUID> ids = uuidService.parseExpenseIds(categories, ",");
            log.info("Parsed Category IDs to Hide: " + ids.toString());
            categoryRepository.hideAllById(ids);
        } else {
            log.info("No categories to hide");
        }

        if (shops != null && !shops.isEmpty()) {
            List<UUID> ids = uuidService.parseExpenseIds(shops, ",");
            log.info("Parsed Shop IDs to Hide: " + ids.toString());
            shopRepository.hideAllById(ids);
        } else {
            log.info("No shops to hide");
        }

        return "redirect:/expenses";
    }
}
