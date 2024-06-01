package pl.edu.s28201.webExpenses.service.currency;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public interface CurrencyService {

    Map<String, String> findAllCurrenciesCodeToNameMap();

    BigDecimal getCurrencyToUsdRate(Currency currency);
}
