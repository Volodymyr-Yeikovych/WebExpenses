package pl.edu.s28201.webExpenses.service;

import ch.qos.logback.core.joran.sanity.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.dto.ChartDto;
import pl.edu.s28201.webExpenses.exception.InvalidBarChartParameterException;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ChartService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ChartService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Pair<List<String>, List<BigDecimal>> getChartDataFromParameters(ChartDto dto) {
        var labels = getChatLabels(dto.getFrom(), dto.getTo(), dto.getBarCount());


        return null;
    }

    private Object getChatLabels(LocalDate from, LocalDate to, int barCount) {
        long daysDiff = ChronoUnit.DAYS.between(from, to);
        if (daysDiff < 12) throw new InvalidBarChartParameterException("Dates should have at least 12 days difference.");

        if (barCount < 3 || barCount > 12) throw new InvalidBarChartParameterException("Number of Bar Chart Columns can't be less than 3 or more than 12.");

        BigDecimal decimalDiff = BigDecimal.valueOf(daysDiff);
        BigDecimal step = decimalDiff.divide(BigDecimal.valueOf(barCount), RoundingMode.DOWN);
        // last range: 11steps + 12th step + remaining days


        return null;
    }

}
