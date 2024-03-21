package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.s28201.webExpenses.model.ExpenseShop;
import pl.edu.s28201.webExpenses.repository.ExpenseShopRepository;
import pl.edu.s28201.webExpenses.service.SecurityService;

@Controller
@Slf4j
@RequestMapping("/shop")
public class NewShopController {

    private final SecurityService securityService;
    private final ExpenseShopRepository shopRepository;

    @Autowired
    public NewShopController(SecurityService securityService, ExpenseShopRepository shopRepository) {
        this.securityService = securityService;
        this.shopRepository = shopRepository;
    }

    @GetMapping
    public String displayNewShop(Model model) {
        log.info("GET: Inside displayNewShop()");
        model.addAttribute("expenseShop", new ExpenseShop());
        return "newShop";
    }

    @PostMapping
    public String returnReadyShop(@ModelAttribute ExpenseShop shop,
                                      @RequestParam("name") String name) {
        log.info("POST: Inside returnReadyShop()");

        shop.setName(name);
        shop.setUser(securityService.getUserFromSecurity());

//        shopRepository.save(shop);

        return "redirect:/expenses";
    }

    @ModelAttribute("expenseCategory")
    public ExpenseShop expense() {
        return new ExpenseShop();
    }
}
