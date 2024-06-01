package pl.edu.s28201.webExpenses.model.expense;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.validation.time.DateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Expense expense = (Expense) o;
        return getId() != null && Objects.equals(getId(), expense.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
