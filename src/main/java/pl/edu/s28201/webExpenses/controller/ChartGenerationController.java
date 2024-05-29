package pl.edu.s28201.webExpenses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.edu.s28201.webExpenses.dto.ChartDto;
import pl.edu.s28201.webExpenses.service.ChartService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/chart")
public class ChartGenerationController {

    private final ChartService chartService;

    @Autowired
    public ChartGenerationController(ChartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/display")
    public String displayChart(
            Model model,
            @ModelAttribute("data") List<BigDecimal> data,
            @ModelAttribute("labels") List<String> labels
            ) {
//        var labels = Arrays.asList("Column 1", "Column 2", "Column 3", "Column 4", "Column 5", "Column 6", "Column 7", "Column $");
//        var data = Arrays.asList(12, 19, 3, 5, 2, 3, 7, 36);

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "charts";
    }

    @GetMapping
    public String displayChartParametersSelection(Model model) {
        model.addAttribute("dto", new ChartDto());

        return "generateChart";
    }

    @PostMapping
    public String returnChartParameters(Model model, @ModelAttribute ChartDto dto, RedirectAttributes ra) {

        chartService.getChartDataFromParameters(dto);

        return "redirect:/chart/display";
    }
}
