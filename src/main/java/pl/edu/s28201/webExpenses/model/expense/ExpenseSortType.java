package pl.edu.s28201.webExpenses.model.expense;

public enum ExpenseSortType {
    DATE_ASC("date_asc"),
    DATE_DESC("date_desc"),
    MONEY_TO_USD_ASC("money_to_usd_asc"),
    MONEY_TO_USD_DESC("money_to_usd_desc"),
    SHOP_ABC_ASC("shop_asc"),
    SHOP_ABC_DESC("shop_desc"),
    CATEGORY_ABC_ASC("category_asc"),
    CATEGORY_ABC_DESC("category_desc");

    private final String sortType;

    ExpenseSortType(String sortType) {
        this.sortType = sortType;
    }

    public static ExpenseSortType fromString(String strSort) {
        return switch (strSort) {
            case "date_asc" -> DATE_ASC;
            case "date_desc" -> DATE_DESC;
            case "money_to_usd_asc" -> MONEY_TO_USD_ASC;
            case "money_to_usd_desc" -> MONEY_TO_USD_DESC;
            case "category_asc" -> CATEGORY_ABC_ASC;
            case "category_desc" -> CATEGORY_ABC_DESC;
            case "shop_asc" -> SHOP_ABC_ASC;
            case "shop_desc" -> SHOP_ABC_DESC;
            default -> throw new IllegalArgumentException("Passed sort type is not supported: " + strSort);
        };
    }
}
