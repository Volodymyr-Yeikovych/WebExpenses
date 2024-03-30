package pl.edu.s28201.webExpenses.model.expense;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.s28201.webExpenses.model.AppUser;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(targetEntity = AppUser.class)
    private AppUser user;
    private String name;
    private boolean isHidden;

    public ExpenseCategory(String name, AppUser user) {
        this.user = user;
        this.name = name;
    }
}
