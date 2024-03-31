package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.s28201.webExpenses.service.CategoryService;
import pl.edu.s28201.webExpenses.service.ShopService;

@Controller
@Slf4j
@RequestMapping("/edit")
public class DeleteCategoryShopController {

    private final CategoryService categoryService;
    private final ShopService shopService;

    public DeleteCategoryShopController(CategoryService categoryService,
                                        ShopService shopService) {
        this.categoryService = categoryService;
        this.shopService = shopService;
    }

    @GetMapping
    public String displayDeleteCategoryShopPage(Model model) {
        log.info("GET: Inside displayDeleteCategoryShopPage()");

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("shops", shopService.findAll());

        return "deleteShopCategory";
    }

    @PostMapping
    public String returnExpensesPage(@RequestParam(value = "selectedCategories", defaultValue = "") String categories,
                                     @RequestParam(value = "selectedShops", defaultValue = "") String shops) {
        log.info("POST: Inside returnExpensesPage()");

        categoryService.hideAllById(categories);
        shopService.hideAllById(shops);

        return "redirect:/expenses";
    }
}
