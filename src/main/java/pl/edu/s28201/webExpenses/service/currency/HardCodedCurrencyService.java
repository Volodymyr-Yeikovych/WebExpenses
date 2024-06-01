package pl.edu.s28201.webExpenses.service.currency;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.exception.CurrencyNotSupportedException;

import java.math.BigDecimal;
import java.util.*;

@Profile("currency-static")
@Service
public class HardCodedCurrencyService implements CurrencyService {

    private final static Set<Currency> SUPPORTED_CURRENCIES = new HashSet<>();

    static {
        SUPPORTED_CURRENCIES.add(Currency.getInstance("USD"));
        SUPPORTED_CURRENCIES.add(Currency.getInstance("UAH"));
        SUPPORTED_CURRENCIES.add(Currency.getInstance("PLN"));
        SUPPORTED_CURRENCIES.add(Currency.getInstance("EUR"));
        SUPPORTED_CURRENCIES.add(Currency.getInstance("CAD"));
        SUPPORTED_CURRENCIES.add(Currency.getInstance("GBP"));
        SUPPORTED_CURRENCIES.add(Currency.getInstance("CZK"));
    }

    @Override
    public Map<String, String> findAllCurrenciesCodeToNameMap() {
        Map<String, String> currencyMap = new HashMap<>();
        for (Currency currency : SUPPORTED_CURRENCIES) {
            currencyMap.put(currency.getCurrencyCode(), currency.getDisplayName());
        }
        return currencyMap;
    }

    @Override
    public BigDecimal getCurrencyToUsdRate(Currency currency) {
        if (currency == null) throw new NullPointerException("Expected currency was null.");
        String code = currency.getCurrencyCode();
        return switch (code) {
            case "USD" -> BigDecimal.ONE;
            case "UAH" -> BigDecimal.valueOf(0.03);
            case "PLN" -> BigDecimal.valueOf(0.25);
            case "EUR" -> BigDecimal.valueOf(1.08);
            case "CAD" -> BigDecimal.valueOf(0.74);
            case "GBP" -> BigDecimal.valueOf(1.26);
            case "CZK" -> BigDecimal.valueOf(0.04);
            default ->
                    throw new CurrencyNotSupportedException("Parsing passed currency [" + code + "] to USD is currently not supported");
        };
    }
}
