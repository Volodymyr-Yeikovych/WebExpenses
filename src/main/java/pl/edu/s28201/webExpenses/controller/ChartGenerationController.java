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
import pl.edu.s28201.webExpenses.model.BarChartParameter;
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
        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "charts";
    }

    @GetMapping
    public String displayChartParametersSelection(Model model) {
        model.addAttribute("dto", new ChartDto());
        model.addAttribute("chartParams", BarChartParameter.values());

        return "generateChart";
    }

    @PostMapping
    public String returnChartParameters(Model model, @ModelAttribute ChartDto dto, RedirectAttributes ra) {

        var chartData = chartService.getChartDataFromParameters(dto);

        ra.addFlashAttribute("name", chartData.getChartName());
        ra.addFlashAttribute("labels", chartData.getLabels());
        ra.addFlashAttribute("data", chartData.getDecimalValues());

        return "redirect:/chart/display";
    }
}
