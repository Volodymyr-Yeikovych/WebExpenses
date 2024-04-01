package pl.edu.s28201.webExpenses.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.exception.SortTypeNotSupportedException;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.dto.ExpenseDto;
import pl.edu.s28201.webExpenses.model.dto.FilterDto;
import pl.edu.s28201.webExpenses.model.expense.Expense;
import pl.edu.s28201.webExpenses.model.expense.ExpenseCategory;
import pl.edu.s28201.webExpenses.model.expense.ExpenseShop;
import pl.edu.s28201.webExpenses.model.expense.ExpenseSortType;
import pl.edu.s28201.webExpenses.repository.CurrencyRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ExpenseService {

    private final CurrencyRepository currencyRepository;
    private final ExpenseRepository expenseRepository;
    private final UuidService uuidService;
    private final SecurityService securityService;
    private List<Expense> filterPool = new ArrayList<>();
    private boolean isFiltered = false;

    @Autowired
    public ExpenseService(CurrencyRepository currencyRepository, ExpenseRepository expenseRepository, UuidService uuidService, SecurityService securityService) {
        this.currencyRepository = currencyRepository;
        this.expenseRepository = expenseRepository;
        this.uuidService = uuidService;
        this.securityService = securityService;
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

    private String formatExpenseDate(LocalDateTime localDateTime) {
        return localDateTime.getYear() + ": " + localDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.UK) + " " + localDateTime.getDayOfMonth();
    }

    private String formatMoneySpent(BigDecimal amount, Currency currency) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        return new DecimalFormat("#,###.##", symbols).format(amount) + " " + currency.getSymbol();
    }

    private boolean isValidExpenseId(UUID id, AppUser currentUser) {
        Optional<Expense> expOpt = expenseRepository.findById(id);

        if (expOpt.isEmpty()) return false;

        Expense exp = expOpt.get();
        return exp.getUser().equals(currentUser);
    }

    public void deleteAllById(String selectedExpensesIds) {
        if (selectedExpensesIds != null && !selectedExpensesIds.isEmpty()) {
            List<UUID> ids = uuidService.parseExpenseIds(selectedExpensesIds, ",");
            StringBuilder deleteMsg = new StringBuilder("Parsed Expenses IDs to Delete: [");
            for (var id : ids) {
                if (isValidExpenseId(id, securityService.getUserFromSecurity())) {
                    Optional<Expense> exp = expenseRepository.findById(id);
                    expenseRepository.deleteById(id);
                    deleteMsg.append(id).append(",");
                    exp.ifPresent(expense -> filterPool.remove(expense));
                }
            }
            deleteMsg.append("]");
            log.info(deleteMsg.toString());
        } else {
            log.info("No expenses selected");
        }
    }

    private List<Expense> sortAndRetrieveExpenses(String strSort) {
        ExpenseSortType sortType = getSortTypeFromString(strSort);

        return getSortedExpenses(sortType);
    }

    private List<Expense> getSortedExpenses(ExpenseSortType sortType) {
        AppUser user = securityService.getUserFromSecurity();

        if (isFiltered) return getSortedFilteredExpenses(sortType);

        return switch (sortType) {
            case DATE_ASC -> expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.ASC, "timeMade"));
            default -> expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.DESC, "timeMade"));

            case CATEGORY_ABC_ASC ->
                    expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.ASC, "category.name"));
            case CATEGORY_ABC_DESC ->
                    expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.DESC, "category.name"));

            case SHOP_ABC_ASC -> expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.ASC, "shop.name"));
            case SHOP_ABC_DESC -> expenseRepository.findExpensesByUser(user, Sort.by(Sort.Direction.DESC, "shop.name"));

            case MONEY_TO_USD_ASC -> sortByMoneyAsc(expenseRepository.findExpensesByUser(user));
            case MONEY_TO_USD_DESC -> sortByMoneyDesc(expenseRepository.findExpensesByUser(user));
        };
    }

    private List<Expense> getSortedFilteredExpenses(ExpenseSortType sortType) {
        return switch (sortType) {
            case DATE_ASC -> sortByDateAsc(filterPool);
            default -> sortByDateDesc(filterPool);

            case CATEGORY_ABC_ASC -> sortByCatAsc(filterPool);
            case CATEGORY_ABC_DESC -> sortByCatDesc(filterPool);

            case SHOP_ABC_ASC -> sortByShopAsc(filterPool);
            case SHOP_ABC_DESC -> sortByShopDesc(filterPool);

            case MONEY_TO_USD_ASC -> sortByMoneyAsc(filterPool);
            case MONEY_TO_USD_DESC -> sortByMoneyDesc(filterPool);
        };
    }

    private List<Expense> sortByShopDesc(List<Expense> filterPool) {
        List<Expense> asc = new ArrayList<>(sortByShopAsc(filterPool));
        Collections.reverse(asc);
        return asc;
    }

    private List<Expense> sortByShopAsc(List<Expense> filterPool) {
        return filterPool.stream().sorted(Comparator.comparing(expense -> expense.getShop().getName())).collect(Collectors.toList());
    }

    private List<Expense> sortByCatDesc(List<Expense> filterPool) {
        List<Expense> asc = new ArrayList<>(sortByCatAsc(filterPool));
        Collections.reverse(asc);
        return asc;
    }

    private List<Expense> sortByCatAsc(List<Expense> filterPool) {
        return filterPool.stream().sorted(Comparator.comparing(expense -> expense.getCategory().getName())).collect(Collectors.toList());
    }

    private List<Expense> sortByDateDesc(List<Expense> filterPool) {
        return filterPool.stream().sorted(Comparator.comparing(Expense::getTimeMade).reversed()).collect(Collectors.toList());
    }

    private List<Expense> sortByDateAsc(List<Expense> filterPool) {
        return filterPool.stream().sorted(Comparator.comparing(Expense::getTimeMade)).collect(Collectors.toList());
    }

    public BigDecimal getMinPrice() {
        var listE = getSortedExpenses(ExpenseSortType.MONEY_TO_USD_ASC);
        if (listE.isEmpty()) return BigDecimal.valueOf(100);

        Expense e = listE.getFirst();
        if (e == null) return BigDecimal.valueOf(100);
        return e.getMoneySpent();
    }

    public BigDecimal getMaxPrice() {
        var listE = getSortedExpenses(ExpenseSortType.MONEY_TO_USD_ASC);
        if (listE.isEmpty()) return BigDecimal.valueOf(0);

        Expense e = listE.getLast();
        if (e == null) return BigDecimal.valueOf(0);

        return e.getMoneySpent();
    }

    public LocalDateTime getMinDate() {
        var listE = getSortedExpenses(ExpenseSortType.DATE_ASC);
        if (listE.isEmpty()) return LocalDateTime.MIN;

        var t = listE.getFirst();
        if (t == null) return LocalDateTime.MIN;
        return t.getTimeMade();
    }


    public LocalDateTime getMaxDate() {
        var listE = getSortedExpenses(ExpenseSortType.DATE_ASC);
        if (listE.isEmpty()) return LocalDateTime.MIN;

        var t = listE.getLast();
        if (t == null) return LocalDateTime.MIN;
        return t.getTimeMade();
    }

    public void filter(List<ExpenseCategory> filterCats, List<ExpenseShop> filterShops, LocalDateTime from, LocalDateTime till, FilterDto dto) {
        AppUser user = securityService.getUserFromSecurity();

        Stream<Expense> result = expenseRepository.findExpensesByUser(user)
                .stream();

        if (filterCats != null && !filterCats.isEmpty()) {
            result = result.filter(expense -> filterCats.contains(expense.getCategory()));
        }

        if (filterShops != null && !filterShops.isEmpty()) {
            result = result.filter(expense -> filterShops.contains(expense.getShop()));
        }

        int min = Math.min(dto.getMinPrice(), dto.getMaxPrice());
        int max = Math.max(dto.getMinPrice(), dto.getMaxPrice());

        result = result
                .filter(expense -> isDateBetween(expense.getTimeMade(), from, till))
                .filter(expense -> isAmountBetween(expense.getMoneySpent(), min, max));

        filterPool = result.collect(Collectors.toList());
    }

    private boolean isAmountBetween(BigDecimal d, int min, int max) {
        return d.intValue() >= min && d.intValue() <= max;
    }

    private boolean isDateBetween(LocalDateTime dateTime, LocalDateTime from, LocalDateTime till) {
        return dateTime.isBefore(from) && dateTime.isAfter(till);
    }

    public List<ExpenseDto> parseToExpenseDto(List<Expense> expenses) {
        List<ExpenseDto> dtos = new ArrayList<>();
        for (var e : expenses) {
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

    public List<ExpenseDto> parseToExpenseDto(String sortType) {
        List<Expense> expenses = sortAndRetrieveExpenses(sortType);

        return parseToExpenseDto(expenses);
    }

    public void setFiltered() {
        isFiltered = true;
    }

    public void setUnFiltered() {
        isFiltered = false;
    }

    public boolean isFiltered() {
        return isFiltered;
    }
}
