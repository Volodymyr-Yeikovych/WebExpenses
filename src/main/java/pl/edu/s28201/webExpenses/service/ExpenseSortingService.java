package pl.edu.s28201.webExpenses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.exception.SortTypeNotSupportedException;
import pl.edu.s28201.webExpenses.model.expense.Expense;
import pl.edu.s28201.webExpenses.model.expense.ExpenseSortType;
import pl.edu.s28201.webExpenses.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ExpenseSortingService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public ExpenseSortingService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public ExpenseSortType getSortTypeFromString(String strSort) {
        return switch (strSort) {
            case "date_asc" -> ExpenseSortType.DATE_ASC;
            case "date_desc" -> ExpenseSortType.DATE_DESC;

            case "money_to_usd_asc" -> ExpenseSortType.MONEY_TO_USD_ASC;
            case "money_to_usd_desc" -> ExpenseSortType.MONEY_TO_USD_DESC;

            case "category_asc" -> ExpenseSortType.CATEGORY_ABC_ASC;
            case "category_desc" -> ExpenseSortType.CATEGORY_ABC_DESC;

            case "shop_asc" -> ExpenseSortType.SHOP_ABC_ASC;
            case "shop_desc" -> ExpenseSortType.SHOP_ABC_DESC;

            default -> throw new SortTypeNotSupportedException("Passed sort type is not supported: " + strSort);
        };
    }

    public List<Expense> sortByMoneyAsc(List<Expense> expenses) {
        return expenses
                .stream()
                .sorted(
                        Comparator.comparing(expense -> expense
                                .getMoneySpent()
                                .multiply(BigDecimal.valueOf(currencyRepository
                                        .getCurrencyToUsdRate(expense.getCurrency())))))
                .toList();
    }

    public List<Expense> sortByMoneyDesc(List<Expense> expenses) {
        List<Expense> asc = new ArrayList<>(sortByMoneyAsc(expenses));
        Collections.reverse(asc);
        return asc;
    }
}
