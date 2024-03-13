package pl.edu.s28201.webExpenses.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
public class ExpenseParsingService {

    public static String formatExpenseDate(LocalDateTime localDateTime) {
        return localDateTime.getYear() + ": " + localDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.UK) + " " + localDateTime.getDayOfMonth();
    }
}
