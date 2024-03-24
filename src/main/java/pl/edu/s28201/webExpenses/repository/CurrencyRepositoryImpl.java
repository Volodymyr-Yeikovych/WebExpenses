package pl.edu.s28201.webExpenses.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import pl.edu.s28201.webExpenses.exception.CurrencyNotSupportedException;

import java.util.*;

@Profile("currency-static")
@Repository
public class CurrencyRepositoryImpl implements CurrencyRepository {

    @Override
    public Set<Currency> findAll() {
        return getSupportedCurrencies();
    }

    @Override
    public Map<String, String> findAllCurrenciesCodeToNameMap() {
        Map<String, String> currencyMap = new HashMap<>();
        for (Currency currency : findAll()) {
            currencyMap.put(currency.getCurrencyCode(), currency.getDisplayName());
        }
        return currencyMap;
    }

    private Set<Currency> getSupportedCurrencies() {
        Set<Currency> currencies = new HashSet<>();
        currencies.add(Currency.getInstance("USD"));
        currencies.add(Currency.getInstance("UAH"));
        currencies.add(Currency.getInstance("PLN"));
        currencies.add(Currency.getInstance("EUR"));
        currencies.add(Currency.getInstance("CAD"));
        currencies.add(Currency.getInstance("GBP"));
        currencies.add(Currency.getInstance("CZK"));
        return currencies;
    }

    @Override
    public double getCurrencyToUsdRate(Currency currency) {
        if (currency == null) throw new NullPointerException("Expected currency was null.");
        String code = currency.getCurrencyCode();
        return switch (code) {
            case "USD" -> 1;
            case "UAH" ->  0.03;
            case "PLN" -> 0.25;
            case "EUR" -> 1.08;
            case "CAD" -> 0.74;
            case "GBP" -> 1.26;
            case "CZK" -> 0.04;
            default -> throw new CurrencyNotSupportedException("Parsing passed currency [" + code + "] to USD is currently not supported");
        };
    }
}
