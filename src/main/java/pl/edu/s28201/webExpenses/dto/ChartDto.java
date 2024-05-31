package pl.edu.s28201.webExpenses.dto;

import lombok.Data;
import pl.edu.s28201.webExpenses.model.BarChartParameter;

import java.time.LocalDate;

@Data
public class ChartDto {
    // TODO: FROM - TO >= barChart!!!!
    private LocalDate from;
    private LocalDate to;
    private BarChartParameter parameter;
    private int barCount;
}
