package pl.edu.s28201.webExpenses.model.expense;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.validation.time.DateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Expense {

    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @ManyToOne(targetEntity = AppUser.class)
    private AppUser user;
    @DateTime
    private LocalDateTime timeMade;
    private BigDecimal moneySpent;
    private Currency currency;
    @ManyToOne(targetEntity = ExpenseCategory.class)
    private ExpenseCategory category;
    @ManyToOne(targetEntity = ExpenseShop.class)
    private ExpenseShop shop;
    @Size(max = 254, message = "Description cannot be longer than 254 characters")
    private String description;

    public Expense(AppUser user, LocalDateTime timeMade, BigDecimal moneySpent, Currency currency, ExpenseCategory category, ExpenseShop shop, String description) {
        this.user = user;
        this.timeMade = timeMade;
        this.moneySpent = moneySpent;
        this.currency = currency;
        this.category = category;
        this.shop = shop;
        this.description = description;
    }
}
