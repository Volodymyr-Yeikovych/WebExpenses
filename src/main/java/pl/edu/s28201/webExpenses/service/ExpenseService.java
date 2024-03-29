package pl.edu.s28201.webExpenses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.exception.SortTypeNotSupportedException;
import pl.edu.s28201.webExpenses.model.dto.ExpenseDto;
import pl.edu.s28201.webExpenses.model.expense.Expense;
import pl.edu.s28201.webExpenses.model.expense.ExpenseSortType;
import pl.edu.s28201.webExpenses.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ExpenseService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public ExpenseService(CurrencyRepository currencyRepository) {
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

    public List<UUID> parseExpenseIds(String string, String separator) {
        return Arrays.stream(string.split(separator)).map(UUID::fromString).toList();
    }

    public List<ExpenseDto> parseToExpenseDto(List<Expense> expenses) {
        List<ExpenseDto> dtos = new ArrayList<>();
        for (Expense e : expenses) {
            String parsedTime = formatExpenseDate(e.getTimeMade());
            String moneyAndCurrency = formatMoneySpent(e.getMoneySpent(), e.getCurrency());

            ExpenseDto dto = ExpenseDto.builder()
                    .id(e.getId())
                    .timeMade(parsedTime)
                    .moneyAndCurrency(moneyAndCurrency)
                    .shop(e.getShop().getName())
                    .category(e.getCategory().getName())
                    .description(e.getDescription())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

    private String formatExpenseDate(LocalDateTime localDateTime) {
        return localDateTime.getYear() + ": " + localDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.UK) + " " + localDateTime.getDayOfMonth();
    }

    private String formatMoneySpent(BigDecimal amount, Currency currency) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        return new DecimalFormat("#,###.##", symbols).format(amount) + " " + currency.getSymbol();
    }
}
