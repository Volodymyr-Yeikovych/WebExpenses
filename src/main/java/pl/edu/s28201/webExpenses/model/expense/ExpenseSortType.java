package pl.edu.s28201.webExpenses.model.expense;

import java.util.HashMap;
import java.util.Map;

public enum ExpenseSortType {
    DATE_ASC("date_asc"),
    DATE_DESC("date_desc"),
    MONEY_TO_USD_ASC("money_to_usd_asc"),
    MONEY_TO_USD_DESC("money_to_usd_desc"),
    SHOP_ABC_ASC("shop_asc"),
    SHOP_ABC_DESC("shop_desc"),
    CATEGORY_ABC_ASC("category_asc"),
    CATEGORY_ABC_DESC("category_desc");

    private static final Map<String, ExpenseSortType> BY_SORT_TYPE = new HashMap<>();

    static {
        for (ExpenseSortType type : values()) {
            BY_SORT_TYPE.put(type.sortType, type);
        }
    }

    private final String sortType;

    ExpenseSortType(String sortType) {
        this.sortType = sortType;
    }

    public static ExpenseSortType fromString(String strSort) {
        return BY_SORT_TYPE.get(strSort);
    }
}
