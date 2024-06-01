package pl.edu.s28201.webExpenses.service.fake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.expense.Expense;
import pl.edu.s28201.webExpenses.model.expense.ExpenseCategory;
import pl.edu.s28201.webExpenses.model.expense.ExpenseShop;
import pl.edu.s28201.webExpenses.repository.AppUserRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseCategoryRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseShopRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class MockService {

    public static final int MOCK_DECIMAL_LOWER_BOUND = 0;
    public static final int MOCK_DECIMAL_UPPER_BOUND = 1_000_000;
    public static final int MOCK_NUMBER = 10;

    private final AppUserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseShopRepository shopRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public MockService(AppUserRepository userRepository, ExpenseRepository expenseRepository, ExpenseCategoryRepository categoryRepository, ExpenseShopRepository shopRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.shopRepository = shopRepository;
        this.encoder = encoder;
    }

    public void createMocksIfNeeded() {
        if (userRepository.findAll().isEmpty()) createMocks();
    }

    private void createMocks() {
        List<AppUser> users = mockUsers();

        for (AppUser user : users) {
            log.info(user.toString());
            mockExpenses(user, expenseRepository, categoryRepository, shopRepository);
        }
    }

    private List<AppUser> mockUsers() {
        List<AppUser> users = new ArrayList<>();

        AppUser user1 = new AppUser("email1@example.com", "John", "Doe", encoder.encode("psw"));
        users.add(user1);
        userRepository.save(user1);

        AppUser user2 = new AppUser("email2@example.com", "Alice", "Smith", encoder.encode("psw"));
        users.add(user2);
        userRepository.save(user2);

        AppUser user3 = new AppUser("email3@example.com", "Bob", "Johnson", encoder.encode("psw"));
        users.add(user3);
        userRepository.save(user3);

        AppUser user4 = new AppUser("email4@example.com", "Emma", "Brown", encoder.encode("psw"));
        users.add(user4);
        userRepository.save(user4);

        AppUser user5 = new AppUser("email5@example.com", "James", "Brown", encoder.encode("psw"));
        users.add(user5);
        userRepository.save(user5);

        return users;
    }

    private void mockExpenses(AppUser user,
                              ExpenseRepository expenseRepository,
                              ExpenseCategoryRepository categoryRepository,
                              ExpenseShopRepository shopRepository) {
        List<BigDecimal> prices = mockPrices();
        List<ExpenseCategory> categories = mockCategories(categoryRepository, user);
        List<ExpenseShop> shops = mockShops(shopRepository, user);
        List<String> descriptions = mockDescriptions();

        for (int i = 0; i < MOCK_NUMBER; i++) {
            Expense expense = new Expense(user, LocalDateTime.now(), prices.get(i),
                    Currency.getInstance("USD"), categories.get(i), shops.get(i), descriptions.get(i));
            expenseRepository.save(expense);
        }
    }

    private List<ExpenseCategory> mockCategories(ExpenseCategoryRepository repository, AppUser user) {
        List<ExpenseCategory> categories = new ArrayList<>();

        for (int i = 1; i <= MOCK_NUMBER; i++) {
            ExpenseCategory category = new ExpenseCategory("Category_" + i, user);
            categories.add(category);
            repository.save(category);
        }

        return categories;
    }

    private List<BigDecimal> mockPrices() {
        List<BigDecimal> prices = new ArrayList<>();

        for (int i = 0; i < MOCK_NUMBER; i++) {
            prices.add(BigDecimal.valueOf(new Random().nextDouble(MOCK_DECIMAL_LOWER_BOUND, MOCK_DECIMAL_UPPER_BOUND)));
        }

        return prices;
    }

    private List<ExpenseShop> mockShops(ExpenseShopRepository repository, AppUser user) {
        List<ExpenseShop> shops = new ArrayList<>();

        for (int i = 1; i <= MOCK_NUMBER; i++) {
            ExpenseShop shop = new ExpenseShop("Shop_" + i, user);
            shops.add(shop);
            repository.save(shop);
        }

        return shops;
    }

    private List<String> mockDescriptions() {
        List<String> descriptions = new ArrayList<>();

        for (int i = 1; i <= MOCK_NUMBER; i++) {
            String desc = "Long and informative informative informative informative informative informative informative informative informative informative informative informative  informative informative informative Description_" + i;
            descriptions.add(desc);
        }

        return descriptions;
    }
}
