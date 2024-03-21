package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.s28201.webExpenses.model.ExpenseDto;
import pl.edu.s28201.webExpenses.model.ExpenseShop;

//@Controller
//@Slf4j
//@RequestMapping("/expenses/delete")
//public class DeleteExpensesController {
//
//    @GetMapping
//    public String displayNewShop(Model model) {
//        log.info("GET: Inside displayNewShop()");
//        return "deleteExpenses";
//    }
//
//    @PostMapping
//    public String returnReadyShop(@ModelAttribute ExpenseDto dto,
//                                  @RequestParam("name") String name) {
//        log.info("POST: Inside returnReadyShop()");
//
//        dto.setName(name);
//        dto.setUser(securityService.getUserFromSecurity());
//
//        shopRepository.save(dto);
//
//        return "redirect:/expenses";
//    }
//
//    @ModelAttribute("expenseCategory")
//}
