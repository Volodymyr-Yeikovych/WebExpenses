package pl.edu.s28201.webExpenses.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Profile("currency-static")
@Repository
public class CurrencyRepositoryImpl implements CurrencyRepository{

    @Override
    public Set<Currency> findAll() {
        return Currency.getAvailableCurrencies();
    }

    @Override
    public Map<String, String> findAllCurrenciesCodeToNameMap() {
        Map<String, String> currencyMap = new HashMap<>();
        for (Currency currency : findAll()) {
            currencyMap.put(currency.getCurrencyCode(), currency.getDisplayName());
        }
        return currencyMap;
    }
}
