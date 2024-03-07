package pl.edu.s28201.webExpenses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.repository.AppUserRepository;
import pl.edu.s28201.webExpenses.service.PasswordEncoderService;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@Slf4j
@EnableJpaRepositories(basePackageClasses = AppUserRepository.class)
public class WebExpensesApplication {

    private final PasswordEncoderService passwordService;

    public WebExpensesApplication(PasswordEncoderService passwordService) {
        this.passwordService = passwordService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebExpensesApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(AppUserRepository repository) {
        return (args) -> {
            // save a few customers
            repository.save(new AppUser("email1@example.com", "John", "Doe", passwordService.encode("password1")));
            repository.save(new AppUser("email2@example.com", "Alice", "Smith", passwordService.encode("password2")));
            repository.save(new AppUser("email3@example.com", "Bob", "Johnson", passwordService.encode("password3")));
            repository.save(new AppUser("email4@example.com", "Emma", "Brown", passwordService.encode("password4")));
            repository.save(new AppUser("email5@example.com", "James", "Brown", passwordService.encode("password5")));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(customer -> {
                log.info(customer.toString());
            });
            log.info("");

            // fetch an individual customer by ID
            Optional<AppUser> usr = repository.findById(new UUID(0L, 1L));
            log.info("Customer found with findById(1L):");
            log.info("--------------------------------");
            if (usr.isPresent()) {
                log.info(usr.toString());
            } else {
                log.info("User with UUID 1L ---> Was not Found.");
            }
            log.info("");
            // fetch customers by last name
            log.info("Customer found with findByLastName('Brown'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Brown").forEach(appUser -> {
                log.info(appUser.toString());
            });
            log.info("");
        };
    }
}
