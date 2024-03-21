package pl.edu.s28201.webExpenses.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ExpenseDto {

    private List<Expense> expenses = new ArrayList<>();

    public void add(Expense expense) {
        expenses.add(expense);
    }

    public void addAll(List<Expense> list) {
        expenses.addAll(list);
    }
}
