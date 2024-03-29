package pl.edu.s28201.webExpenses.service;

import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.expense.ExpenseCategory;
import pl.edu.s28201.webExpenses.model.expense.ExpenseShop;
import pl.edu.s28201.webExpenses.repository.ExpenseCategoryRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseShopRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultUserDataCreationService {

    private final static List<String> DEFAULT_CATEGORIES = new ArrayList<>();
    private final static List<String> DEFAULT_SHOPS = new ArrayList<>();

    private final ExpenseShopRepository shopRepository;
    private final ExpenseCategoryRepository categoryRepository;

    static {
        populateDefaultValues();
    }

    public DefaultUserDataCreationService(ExpenseShopRepository shopRepository, ExpenseCategoryRepository categoryRepository) {
        this.shopRepository = shopRepository;
        this.categoryRepository = categoryRepository;
    }

    private static void populateDefaultValues() {
        DEFAULT_CATEGORIES.add("Groceries");
        DEFAULT_CATEGORIES.add("Clothing");
        DEFAULT_CATEGORIES.add("Electronics");
        DEFAULT_CATEGORIES.add("Home and Kitchen");
        DEFAULT_CATEGORIES.add("Health and Beauty");
        DEFAULT_CATEGORIES.add("Furniture and Home Decor");
        DEFAULT_CATEGORIES.add("Books");
        DEFAULT_CATEGORIES.add("Games");
        DEFAULT_CATEGORIES.add("Sports and Outdoor");
        DEFAULT_CATEGORIES.add("Automotive Supplies");
        DEFAULT_CATEGORIES.add("Pets");
        DEFAULT_CATEGORIES.add("Jewelry and Accessories");
        DEFAULT_CATEGORIES.add("Tools and Hardware");
        DEFAULT_CATEGORIES.add("Office Supplies");
        DEFAULT_CATEGORIES.add("Music and Movies");

        DEFAULT_SHOPS.add("Tesco");
        DEFAULT_SHOPS.add("Lidl");
        DEFAULT_SHOPS.add("IKEA");
        DEFAULT_SHOPS.add("H&M");
        DEFAULT_SHOPS.add("Decathlon");
        DEFAULT_SHOPS.add("Walmart");
        DEFAULT_SHOPS.add("Amazon");
    }

    public void addDefaultDataToUser(AppUser user) {
        addDefaultCategoriesToUser(user);
        addDefaultShopsToUser(user);
    }

    private void addDefaultCategoriesToUser(AppUser user) {
        for (String cat : DEFAULT_CATEGORIES) {
            ExpenseCategory category = new ExpenseCategory(cat, user);
            categoryRepository.save(category);
        }
    }

    private void addDefaultShopsToUser(AppUser user) {
        for (String sh : DEFAULT_SHOPS) {
            ExpenseShop shop = new ExpenseShop(sh, user);
            shopRepository.save(shop);
        }
    }
}
