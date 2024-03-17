//package pl.edu.s28201.webExpenses.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import pl.edu.s28201.webExpenses.model.Expense;
//import pl.edu.s28201.webExpenses.repository.ExpenseRepository;
//
//@Controller
//@RequestMapping("/expenses/new")
//@Slf4j
//public class NewExpenseController {
//
//    private final ExpenseRepository expenseRepository;
//
//    @Autowired
//    public NewExpenseController(ExpenseRepository expenseRepository) {
//        this.expenseRepository = expenseRepository;
//    }
//
//    @GetMapping
//    public String displayNewExpensePage() {
//        log.info("GET: Inside displayNewExpensePage()");
//        return "newExpense";
//    }
//
//    @PostMapping
//    public String returnReadyExpense(@ModelAttribute("expense") Expense expense) {
//        log.info("POST: Inside returnReadyExpense()");
//        expenseRepository.save(expense);
//        return "redirect:/expenses";
//    }
//}
