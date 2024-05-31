package pl.edu.s28201.webExpenses.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import pl.edu.s28201.webExpenses.model.BarChartParameter;
import pl.edu.s28201.webExpenses.validation.time.Date;

import java.time.LocalDate;

@Data
public class ChartDto {
    @Date
    private LocalDate from;
    @Date
    private LocalDate to;
    private BarChartParameter parameter;
    @Min(value = 3)
    @Max(value = 12)
    private int barCount;
}
