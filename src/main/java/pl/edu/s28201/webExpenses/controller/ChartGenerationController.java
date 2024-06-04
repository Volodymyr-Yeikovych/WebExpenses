package pl.edu.s28201.webExpenses.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.edu.s28201.webExpenses.dto.ChartDto;
import pl.edu.s28201.webExpenses.model.BarChartParameter;
import pl.edu.s28201.webExpenses.service.chart.ChartService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/chart")
@Slf4j
public class ChartGenerationController {

    private final ChartService chartService;

    @Autowired
    public ChartGenerationController(
            ChartService chartService
    ) {
        this.chartService = chartService;
    }

    @GetMapping("/display")
    public String displayChart(
            Model model,
            @ModelAttribute("data") List<BigDecimal> data,
            @ModelAttribute("labels") List<String> labels
    ) {
        log.info("GET: /chart/display");

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "charts";
    }

    @GetMapping
    public String displayChartParametersSelection(
            Model model,
            @ModelAttribute("invalidDateRange") String invalidDateRange,
            @ModelAttribute("errors") ArrayList<String> errors
    ) {
        log.info("GET: /chart");

        model.addAttribute("dto", new ChartDto());
        model.addAttribute("chartParams", BarChartParameter.values());
        model.addAttribute("invalidDateRange", invalidDateRange);
        model.addAttribute("errors", errors);

        return "generateChart";
    }

    @PostMapping
    public String returnChartParameters(
            @ModelAttribute @Valid ChartDto dto,
            Errors errors,
            RedirectAttributes ra
    ) {
        log.info("POST: /chart");

        if (errors.hasErrors()) {
            ra.addFlashAttribute("errors", errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
            return "redirect:/chart";
        }

        boolean invalidDate = !chartService.isValidDate(dto.getFrom(), dto.getTo(), dto.getBarCount());

        if (invalidDate) {
            ra.addFlashAttribute("invalidDateRange", "Days range should be more or equal to number of bar chart columns.");
            return "redirect:/chart";
        }

        var chartData = chartService.getChartDataFromParameters(dto);

        ra.addFlashAttribute("name", chartData.getChartName());
        ra.addFlashAttribute("labels", chartData.getLabels());
        ra.addFlashAttribute("data", chartData.getDecimalValues());

        return "redirect:/chart/display";
    }
}
