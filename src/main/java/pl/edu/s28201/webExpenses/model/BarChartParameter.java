package pl.edu.s28201.webExpenses.model;

import lombok.Getter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum BarChartParameter {
    MONEY_AVG("money_avg", "Average Money Spent USD"),
    MONEY_MAX("money_max", "Highest Money Spent USD"),
    MONEY_MIN("money_min", "Least Money Spent USD"),
    PURCHASES_NUM("purchases_num", "Number of Purchases");

    private static final Map<String, BarChartParameter> BY_PARAMETER = new HashMap<>();
    private static final Map<String, BarChartParameter> BY_DESCRIPTION = new HashMap<>();

    static {
        for (BarChartParameter param : values()) {
            BY_PARAMETER.put(param.parameter, param);
            BY_DESCRIPTION.put(param.description, param);
        }
    }

    private final String parameter;
    private final String description;

    BarChartParameter(String parameter, String description) {
        this.parameter = parameter;
        this.description = description;
    }

    public static BarChartParameter fromParamString(String parameter) {
        return BY_PARAMETER.get(parameter);
    }

    public static BarChartParameter fromDescriptionString(String description) throws ParseException {
        var desc = BY_DESCRIPTION.get(description);
        if (desc == null) throw new ParseException("Cannot parse string {" + description + "} as ChartParameter.", 0);
        return desc;
    }

}
