package pl.edu.s28201.webExpenses.dto;

import lombok.Data;
import pl.edu.s28201.webExpenses.model.ChartParameter;

import java.time.LocalDate;

@Data
public class ChartDto {
    // TODO: FROM - TO >= 12!!!!
    private LocalDate from;
    private LocalDate to;
    private ChartParameter parameter;
    private int barCount;
}
