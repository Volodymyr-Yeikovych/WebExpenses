package pl.edu.s28201.webExpenses.service.currency;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.edu.s28201.webExpenses.exception.CurrencyNotSupportedException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Profile("currency-nbp")
@Service
@Slf4j
public class NbpCurrencyService implements CurrencyService {

    private final RestTemplate restTemplate;
    private LocalDate lastUpdate = LocalDate.MIN;
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

    private final Map<Currency, BigDecimal> currencyExchangeRateToUSD = new HashMap<>();

    @Autowired
    public NbpCurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
        if (!SUPPORTED_CURRENCIES.contains(currency))
            throw new CurrencyNotSupportedException("Currency code[" + currency.getCurrencyCode() + "] is not supported.");

        var daysDiff = Math.abs(ChronoUnit.DAYS.between(lastUpdate, LocalDate.now()));

        if (daysDiff > 2) {
            updateCurrencyMap();
        }

        return currencyExchangeRateToUSD.get(currency);
    }

    private void updateCurrencyMap() {
        lastUpdate = LocalDate.now();
        var usdToPln = getExchangeRateForCode("USD");
        for (Currency c : SUPPORTED_CURRENCIES) {
            BigDecimal rateToUsd;
            var code = c.getCurrencyCode();
            if (code.equals("USD")) {
                rateToUsd = BigDecimal.ONE;
            } else if (code.equals("PLN")) {
                rateToUsd = BigDecimal.ONE.divide(usdToPln, new MathContext(100, RoundingMode.HALF_UP));
            } else {
                var rateToPln = getExchangeRateForCode(code);
                rateToUsd = rateToPln.divide(usdToPln, new MathContext(100, RoundingMode.HALF_UP));
            }
            currencyExchangeRateToUSD.put(c, rateToUsd);
        }
    }

    private BigDecimal getExchangeRateForCode(String code) {
        String urlAndCode = "http://api.nbp.pl/api/exchangerates/rates/A/" + code;
        ResponseEntity<Object> response = restTemplate.getForEntity(urlAndCode, Object.class);

        if (response.getStatusCode().isError()) {
            throw new CurrencyNotSupportedException("Currency code[" + code + "] is not supported.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.convertValue(response.getBody(), JsonNode.class);

        JsonNode firstRate = root.get("rates").get(0);

        return new BigDecimal(firstRate.get("mid").asText());
    }
}
