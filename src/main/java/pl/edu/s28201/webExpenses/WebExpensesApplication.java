package pl.edu.s28201.webExpenses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.s28201.webExpenses.model.*;
import pl.edu.s28201.webExpenses.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@Slf4j
@EnableJpaRepositories(basePackageClasses = AppUserRepository.class)
public class WebExpensesApplication {

    public static final int MOCK_DECIMAL_LOWER_BOUND = 0;
    public static final int MOCK_DECIMAL_UPPER_BOUND = 1_000_000;
    public static final int MOCK_NUMBER = 10;
    private final PasswordEncoder encoder;
    @Autowired
    public WebExpensesApplication(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebExpensesApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(AppUserRepository userRepository,
//                                  UserExpenseRepository userExpenseRepository,
                                  ExpenseRepository expenseRepository,
                                  ExpenseCategoryRepository categoryRepository,
                                  ExpenseOriginRepository originRepository) {
        return (args) -> {
            // save a few customers
            mockUsers(userRepository, expenseRepository, categoryRepository, originRepository);

            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            userRepository.findAll().forEach(customer -> {
                log.info(customer.toString());
            });
            log.info("");

            // fetch an individual customer by ID
            Optional<AppUser> usr = userRepository.findById(new UUID(0L, 1L));
            log.info("Customer found with findById(1L):");
            log.info("--------------------------------");
            if (usr.isPresent()) {
                log.info(usr.toString());
            } else {
                log.info("User with UUID 1L ---> Was not Found.");
            }
            log.info("");
            // fetch customers by last name
//            log.info("Customer found with findByLastName('Brown'):");
            log.info("--------------------------------------------");
//            userRepository.findByLastName("Brown").forEach(appUser -> {
//                log.info(appUser.toString());
//            });
            userRepository.findAll().forEach(appUser -> log.info(appUser.toString()));
            log.info("");

            log.info("Expenses: ");
            List<Expense> expenses = expenseRepository.findAll();
            expenses.forEach(expense -> log.info(expense.toString()));
            log.info("");
         };
    }

    private void mockUsers(AppUserRepository repository, ExpenseRepository expenseRepository, ExpenseCategoryRepository categoryRepository, ExpenseOriginRepository originRepository) {
        List<AppUser> users = new ArrayList<>();

        AppUser user1 = new AppUser("email1@example.com", "John", "Doe", encoder.encode("psw"));
        users.add(user1);
        repository.save(user1);

        AppUser user2 = new AppUser("email2@example.com", "Alice", "Smith", encoder.encode("psw"));
        users.add(user2);
        repository.save(user2);

        AppUser user3 = new AppUser("email3@example.com", "Bob", "Johnson", encoder.encode("psw"));
        users.add(user3);
        repository.save(user3);

        AppUser user4 = new AppUser("email4@example.com", "Emma", "Brown", encoder.encode("psw"));
        users.add(user4);
        repository.save(user4);

        AppUser user5 = new AppUser("email5@example.com", "James", "Brown", encoder.encode("psw"));
        users.add(user5);
        repository.save(user5);

        for (AppUser user : users) {
            log.info(user.toString());

            mockExpenses(user, expenseRepository, categoryRepository, originRepository);
        }
    }

    private void mockExpenses(AppUser user,
                                       ExpenseRepository expenseRepository,
                                       ExpenseCategoryRepository categoryRepository,
                                       ExpenseOriginRepository originRepository) {
        List<BigDecimal> prices = mockPrices();
        List<ExpenseCategory> categories = mockCategory(categoryRepository);
        List<ExpenseOrigin> origins = mockOrigins(originRepository);
        List<String> descriptions = mockDescriptions();

        for (int i = 0; i < MOCK_NUMBER; i++) {
            Expense expense = new Expense(user, LocalDateTime.now(), prices.get(i),
                    Currency.getInstance("USD"), categories.get(i), origins.get(i), descriptions.get(i));
            expenseRepository.save(expense);
        }
    }

    private List<String> mockDescriptions() {
        List<String> descriptions = new ArrayList<>();

        for (int i = 1; i <= MOCK_NUMBER; i++) {
            String desc = "Long and informative Description_" + i;
            descriptions.add(desc);
        }

        return descriptions;
    }

    private List<ExpenseOrigin> mockOrigins(ExpenseOriginRepository repository) {
        List<ExpenseOrigin> origins = new ArrayList<>();

        for (int i = 1; i <= MOCK_NUMBER; i++) {
            ExpenseOrigin origin = new ExpenseOrigin("Origin_" + i);
            origins.add(origin);
            repository.save(origin);
        }

        return origins;
    }

    private List<ExpenseCategory> mockCategory(ExpenseCategoryRepository repository) {
        List<ExpenseCategory> categories = new ArrayList<>();

        for (int i = 1; i <= MOCK_NUMBER; i++) {
            ExpenseCategory category = new ExpenseCategory("Category_" + i);
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
}
