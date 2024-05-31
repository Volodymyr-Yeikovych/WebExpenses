package pl.edu.s28201.webExpenses.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.dto.ChartDto;
import pl.edu.s28201.webExpenses.exception.InvalidBarChartParameterException;
import pl.edu.s28201.webExpenses.model.ChartData;
import pl.edu.s28201.webExpenses.model.BarChartParameter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ChartService {

    private final ChartDataService chartDataService;
    private final static DateTimeFormatter DEFAULT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    @Autowired
    public ChartService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }

    public ChartData getChartDataFromParameters(ChartDto dto) {
        var from = dto.getFrom();
        var to = dto.getTo();
        var barCount = dto.getBarCount();
        var parameter = dto.getParameter();

        long daysDiff = Math.abs(ChronoUnit.DAYS.between(from, to.plusDays(1)));

        log.info("Diff::{}", daysDiff);

        if (daysDiff < barCount)
            throw new InvalidBarChartParameterException("Dates should have at least " + barCount + " days difference.");

        if (barCount < 3 || barCount > 12)
            throw new InvalidBarChartParameterException("Number of Bar Chart Columns can't be less than 3 or more than 12.");

        var format = getViableFormatter(from, to);

        long daysStep = calcStepDays(daysDiff, barCount);

        var minDate = minDate(from, to);
        var maxDate = maxDate(from, to);

        if (minDate.equals(maxDate))
            throw new InvalidBarChartParameterException("Min and Max date should not be equal.");

        List<Long> stepsByBar = calcStepByBar(daysStep, daysDiff, barCount);

        var chartName = getChartName(parameter);
        var labels = getChartLabels(stepsByBar, minDate, format);
        var values = getChartData(stepsByBar, minDate, parameter);

        return ChartData.builder().chartName(chartName).labels(labels).decimalValues(values).build();
    }

    private List<String> getChartData(List<Long> stepsByBar, LocalDate min, BarChartParameter parameter) {
        List<String> result = new ArrayList<>();

        var curFirst = min;
        for (long step : stepsByBar) {
            String data;
            if (step == 1) {
                data = getDataRange(curFirst, curFirst, parameter);
            } else {
                var last = curFirst.plusDays(step - 1);
                data = getDataRange(curFirst, last, parameter);
            }
            curFirst = curFirst.plusDays(step);
            result.add(data);
        }

        return result;
    }

    private String getDataRange(LocalDate start, LocalDate end, BarChartParameter parameter) {
        return switch (parameter) {
            case MONEY_AVG -> chartDataService.getMoneySpentAvg(start, end);
            case MONEY_MAX -> chartDataService.getMoneySpentMax(start, end);
            case MONEY_MIN -> chartDataService.getMoneySpentMin(start, end);
            case PURCHASES_NUM -> chartDataService.getPurchasesNum(start, end);
        };
    }

    private String getChartName(BarChartParameter parameter) {
        return parameter.getDescription();
    }

    private List<Long> calcStepByBar(long daysStep, long totalDays, int barCount) {
        var remainder = totalDays % barCount;
        if (remainder == 0) return evenSteps(daysStep, barCount);

        return unevenSteps(daysStep, barCount, remainder);
    }

    private List<Long> unevenSteps(long daysStep, int barCount, long remainder) {
        var result = new ArrayList<Long>();
        var leftToAssign = remainder;
        for (int i = 0; i < barCount; i++) {
            var step = daysStep;
            if (leftToAssign != 0) {
                step++;
                leftToAssign--;
            }
            result.add(step);
        }
        return result;
    }

    private List<Long> evenSteps(long daysStep, int barCount) {
        var result = new ArrayList<Long>();
        for (int i = 0; i < barCount; i++) {
            result.add(daysStep);
        }
        return result;
    }

    private List<String> getChartLabels(List<Long> stepsByBar, LocalDate min, DateTimeFormatter dateFormatter) {
        List<String> labels = new ArrayList<>();

        var curFirst = min;
        for (long step : stepsByBar) {
            String label;
            if (step == 1) {
                label = String.format("%s", dateFormatter.format(curFirst));
            } else {
                var last = curFirst.plusDays(step - 1);
                label = String.format("%s - %s", dateFormatter.format(curFirst), dateFormatter.format(last));
            }
            curFirst = curFirst.plusDays(step);
            labels.add(label);
        }

        return labels;
    }

    public LocalDate maxDate(LocalDate from, LocalDate to) {
        return from.isAfter(to) ? from : to;
    }

    private DateTimeFormatter getViableFormatter(LocalDate from, LocalDate to) {
        return from.getYear() == to.getYear() ? DateTimeFormatter.ofPattern("MMM dd") : DEFAULT_FORMAT;
    }

    private long calcStepDays(long daysDiff, int barCount) {
        BigDecimal decimalDiff = BigDecimal.valueOf(daysDiff);
        BigDecimal stepDecimal = decimalDiff.divide(BigDecimal.valueOf(barCount), RoundingMode.DOWN);

        return stepDecimal.longValue();
    }

    public LocalDate minDate(LocalDate date1, LocalDate date2) {
        return date1.isBefore(date2) ? date1 : date2;
    }
}
