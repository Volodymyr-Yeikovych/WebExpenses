package pl.edu.s28201.webExpenses.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/expenses/sorted")
public class ExpensesSortController {

    @PostMapping
    public String returnSortedExpenses(@RequestParam("sortType") String sortType, HttpSession session) {
        log.info("POST: Inside returnSortedExpenses()");
        log.info("Sort type selected: {}", sortType);
        session.setAttribute("sortType", sortType);
        return "redirect:/expenses";
    }
}
