package pl.edu.s28201.webExpenses.model;

import java.util.HashMap;
import java.util.Map;

public enum ChartParameter {
    MONEY_AVG("money_avg"),
    MONEY_MAX("money_max"),
    MONEY_MIN("money_min"),
    PURCHASES_MAX("purchases_max"),
    PURCHASES_MIN("purchases_min"),
    SHOP_MAX("shop_max"),
    SHOP_MIN("shop_min"),
    CAT_MAX("cat_max"),
    CAT_MIN("cat_min");

    private static final Map<String, ChartParameter> BY_PARAMETER = new HashMap<>();

    static {
        for (ChartParameter param : values()) {
            BY_PARAMETER.put(param.parameter, param);
        }
    }

    private final String parameter;

    ChartParameter(String parameter) {
        this.parameter = parameter;
    }

    public static ChartParameter fromString(String parameter) {
        return BY_PARAMETER.get(parameter);
    }
}
