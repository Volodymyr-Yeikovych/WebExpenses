package pl.edu.s28201.webExpenses.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.s28201.webExpenses.model.expense.ExpenseCategory;
import pl.edu.s28201.webExpenses.model.expense.ExpenseShop;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {

    private String from;
    private String till;
    private List<ExpenseCategory> selectedCats;
    private List<ExpenseShop> selectedShops;
    private int minPrice;
    private int maxPrice;
}
