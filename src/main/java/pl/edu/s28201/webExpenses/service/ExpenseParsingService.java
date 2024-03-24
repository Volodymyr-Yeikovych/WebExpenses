package pl.edu.s28201.webExpenses.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ExpenseParsingService {

    public static String formatExpenseDate(LocalDateTime localDateTime) {
        return localDateTime.getYear() + ": " + localDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.UK) + " " + localDateTime.getDayOfMonth();
    }

    public static String formatMoneySpent(BigDecimal amount, Currency currency) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        return new DecimalFormat("#,###.##", symbols).format(amount) + " " + currency.getSymbol();
    }

    public static List<UUID> parseExpenseIds(String string, String separator) {
        return Arrays.stream(string.split(separator)).map(UUID::fromString).toList();
    }
}
