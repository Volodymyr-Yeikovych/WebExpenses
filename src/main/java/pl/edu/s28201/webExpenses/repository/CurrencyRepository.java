package pl.edu.s28201.webExpenses.repository;

import java.util.Currency;
import java.util.Map;
import java.util.Set;

public interface CurrencyRepository {

    Set<Currency> findAll();
    Map<String, String> findAllCurrenciesCodeToNameMap();
}
