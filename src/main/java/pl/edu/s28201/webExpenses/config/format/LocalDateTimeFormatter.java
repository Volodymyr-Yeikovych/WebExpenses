package pl.edu.s28201.webExpenses.config.format;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

@Configuration
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
    @Override
    @NonNull
    public LocalDateTime parse(@NonNull String text, @NonNull Locale locale) {
        LocalDate parsedTimeMade = LocalDate.parse(text);
        return parsedTimeMade.atTime(LocalTime.now());
    }

    @Override
    @NonNull
    public String print(@NonNull LocalDateTime object, @NonNull Locale locale) {
        return object.toString();
    }
}
