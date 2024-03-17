package pl.edu.s28201.webExpenses.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class ExpenseOrigin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(targetEntity = AppUser.class)
    private AppUser user;
    private String name;

    public ExpenseOrigin(String name, AppUser user) {
        this.user = user;
        this.name = name;
    }
}
