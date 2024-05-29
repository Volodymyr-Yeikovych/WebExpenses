package pl.edu.s28201.webExpenses.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ExpenseDto {

    private UUID id;
    private String timeMade;
    private String moneyAndCurrency;
    private String category;
    private String shop;
    private String description;
}
