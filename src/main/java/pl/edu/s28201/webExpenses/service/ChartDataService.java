package pl.edu.s28201.webExpenses.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.repository.CurrencyRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChartDataService {

    private final ExpenseRepository expenseRepository;
    private final SecurityService securityService;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public ChartDataService(ExpenseRepository expenseRepository, SecurityService securityService, CurrencyRepository currencyRepository) {
        this.expenseRepository = expenseRepository;
        this.securityService = securityService;
        this.currencyRepository = currencyRepository;
    }

    public String getMoneySpentAvg(LocalDate start, LocalDate end) {
        var expenses = expenseRepository.findExpenseByUserAndTimeMadeBetween(securityService.getUserFromSecurity(), start.atStartOfDay(), end.atTime(23, 59));
        List<BigDecimal> decimals = expenses
                .stream()
                .map(expense -> expense
                        .getMoneySpent()
                        .multiply(BigDecimal
                                .valueOf(currencyRepository
                                        .getCurrencyToUsdRate(expense.getCurrency()))))
                .toList();

        if (decimals.isEmpty()) return "0";

        var sum = BigDecimal.valueOf(0);

        for (var d : decimals) {
            sum = sum.add(d);
        }

        var avg = sum.divide(BigDecimal.valueOf(decimals.size()), RoundingMode.HALF_UP);

        return avg.toString();
    }

    public String getMoneySpentMax(LocalDate start, LocalDate end) {
        var expenses = expenseRepository.findExpenseByUserAndTimeMadeBetween(securityService.getUserFromSecurity(), start.atStartOfDay(), end.atTime(23, 59));
        var max = expenses
                .stream()
                .map(expense -> expense
                        .getMoneySpent()
                        .multiply(BigDecimal
                                .valueOf(currencyRepository
                                        .getCurrencyToUsdRate(expense.getCurrency()))))
                .max(BigDecimal::compareTo);

        if (max.isEmpty()) return "0";

        return max.get().toString();
    }

    public String getMoneySpentMin(LocalDate start, LocalDate end) {
        var expenses = expenseRepository.findExpenseByUserAndTimeMadeBetween(securityService.getUserFromSecurity(), start.atStartOfDay(), end.atTime(23, 59));
        var min = expenses
                .stream()
                .map(expense -> expense
                        .getMoneySpent()
                        .multiply(BigDecimal
                                .valueOf(currencyRepository
                                        .getCurrencyToUsdRate(expense.getCurrency()))))
                .min(BigDecimal::compareTo);

        if (min.isEmpty()) return "0";

        return min.get().toString();
    }

    public String getPurchasesNum(LocalDate start, LocalDate end) {
        var expenses = expenseRepository.findExpenseByUserAndTimeMadeBetween(securityService.getUserFromSecurity(), start.atStartOfDay(), end.atTime(23, 59));
        var maxPurchases = expenses.size();

        return maxPurchases + "";
    }

    public String getShopMax(LocalDate start, LocalDate end) {
        var expenses = expenseRepository.findExpenseByUserAndTimeMadeBetween(securityService.getUserFromSecurity(), start.atStartOfDay(), end.atTime(23, 59));
        var shopNames = expenses.stream().map(e -> e.getShop().getName()).toList();

        if (shopNames.isEmpty()) return "N-A";

        var mostFreqShop = findMostFrequentString(shopNames);

        return mostFreqShop;
    }

    public String getShopMin(LocalDate start, LocalDate end) {
        var expenses = expenseRepository.findExpenseByUserAndTimeMadeBetween(securityService.getUserFromSecurity(), start.atStartOfDay(), end.atTime(23, 59));
        var shopNames = expenses.stream().map(e -> e.getShop().getName()).toList();

        if (shopNames.isEmpty()) return "N-A";

        var leastFreqShop = findLeastFrequentString(shopNames);

        return leastFreqShop;
    }

    public String getCategoryMax(LocalDate start, LocalDate end) {
        var expenses = expenseRepository.findExpenseByUserAndTimeMadeBetween(securityService.getUserFromSecurity(), start.atStartOfDay(), end.atTime(23, 59));
        var catNames = expenses.stream().map(e -> e.getCategory().getName()).toList();

        if (catNames.isEmpty()) return "N-A";

        var mostFreqCat = findMostFrequentString(catNames);

        return mostFreqCat;
    }

    public String getCategoryMin(LocalDate start, LocalDate end) {
        var expenses = expenseRepository.findExpenseByUserAndTimeMadeBetween(securityService.getUserFromSecurity(), start.atStartOfDay(), end.atTime(23, 59));
        var catNames = expenses.stream().map(e -> e.getCategory().getName()).toList();

        if (catNames.isEmpty()) return "N-A";

        var leastFreqCat = findLeastFrequentString(catNames);

        return leastFreqCat;
    }


    public static String findMostFrequentString(List<String> str) {
        Map<String, Long> frequencyMap = getFrequencyMap(str);

        Map.Entry<String, Long> maxEntry = frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        return maxEntry != null ? maxEntry.getKey() : "N-A";
    }

    public static Map<String, Long> getFrequencyMap(List<String> str) {
        return str.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public static String findLeastFrequentString(List<String> str) {
        Map<String, Long> frequencyMap = getFrequencyMap(str);

        Map.Entry<String, Long> minEntry = frequencyMap.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElse(null);

        return minEntry != null ? minEntry.getKey() : "N-A";
    }
}
