package pl.edu.s28201.webExpenses.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.s28201.webExpenses.dto.FilterDto;
import pl.edu.s28201.webExpenses.model.expense.ExpenseCategory;
import pl.edu.s28201.webExpenses.model.expense.ExpenseShop;
import pl.edu.s28201.webExpenses.service.CategoryService;
import pl.edu.s28201.webExpenses.service.ExpenseService;
import pl.edu.s28201.webExpenses.service.ShopService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/expenses/filter")
public class FilterController {

    private final CategoryService categoryService;
    private final ShopService shopService;
    private final ExpenseService expenseService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DEFAULT_SORT_TYPE = "date_desc";
    private static final int SLIDER_STEPS_NUM = 1000;

    @Autowired
    public FilterController(
            CategoryService categoryService,
            ShopService shopService,
            ExpenseService expenseService
    ) {
        this.categoryService = categoryService;
        this.shopService = shopService;
        this.expenseService = expenseService;
    }

    private void fillModel(Model model, int step) {
        model.addAttribute("categories", categoryService.findAllExisting());
        model.addAttribute("shops", shopService.findAllExisting());
        model.addAttribute("step", step);
    }

    @GetMapping
    public String displayFilterPage(Model model) {
        log.info("GET: /expenses/filter");

        int max = expenseService.getMaxPrice().intValue();
        int min = expenseService.getMinPrice().intValue();
        int step = (max - min) / SLIDER_STEPS_NUM;

        FilterDto dto = FilterDto.builder()
                .minPrice(min)
                .maxPrice(max)
                .build();

        model.addAttribute("filter", dto);
        fillModel(model, step);

        return "expenseFilter";
    }

    @PostMapping
    public String returnFilteredExpensePage(
            @ModelAttribute("filter") FilterDto filter,
            Model model
    ) {
        log.info("POST: /expenses/filter");

        LocalDateTime from = getFromDateOfString(filter.getFrom());
        LocalDateTime till = getTillDateOfString(filter.getTill());

        expenseService.setFiltered();

        List<ExpenseCategory> filterCats = filter.getSelectedCats();
        List<ExpenseShop> filterShops = filter.getSelectedShops();

        expenseService.filter(filterCats, filterShops, from, till, filter);

        model.addAttribute("filtered", expenseService.isFiltered());
        model.addAttribute("sortType", DEFAULT_SORT_TYPE);
        model.addAttribute("expenses", expenseService.parseToExpenseDto(DEFAULT_SORT_TYPE));

        return "redirect:/expenses";
    }

    private LocalDateTime getTillDateOfString(String till) {
        if (till == null) return LocalDateTime.MIN;
        try {
            return LocalDate.parse(till, formatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            return LocalDateTime.MIN;
        }
    }

    private LocalDateTime getFromDateOfString(String from) {
        if (from == null) return LocalDateTime.MAX;
        try {
            return LocalDate.parse(from, formatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            return LocalDateTime.MAX;
        }
    }

    @PostMapping("/cancel")
    public String returnUnFilteredExpensesPage() {
        log.info("POST: /cancel");

        expenseService.setUnFiltered();

        return "redirect:/expenses";
    }
}
