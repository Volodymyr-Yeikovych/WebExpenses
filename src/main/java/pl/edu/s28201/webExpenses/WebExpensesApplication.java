package pl.edu.s28201.webExpenses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.edu.s28201.webExpenses.repository.*;
import pl.edu.s28201.webExpenses.service.MockService;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Slf4j
@EnableJpaRepositories(basePackageClasses = AppUserRepository.class)
public class WebExpensesApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebExpensesApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(MockService mockService,
                                  AppUserRepository userRepository,
                                  ExpenseRepository expenseRepository) {
        return (args) -> {
            mockService.createMocksIfNeeded();

            log.info("Users found with findAll():");
            log.info("<<<------------------------------->>>");
            userRepository.findAll().forEach(user -> log.info(user.toString()));
            log.info("<<<-------------------------------->>>");
            log.info("");

            log.info("Expenses: ");
            log.info("<<<-------------------------------->>>");
            expenseRepository.findAll().forEach(expense -> log.info(expense.toString()));
            log.info("<<<-------------------------------->>>");
            log.info("");
        };
    }

}
