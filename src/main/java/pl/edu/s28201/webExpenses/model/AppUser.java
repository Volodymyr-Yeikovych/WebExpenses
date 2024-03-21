package pl.edu.s28201.webExpenses.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class AppUser {

    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "userId", updatable = false, nullable = false)
    private UUID userId;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public AppUser(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public AppUser(UUID userId, String email, String firstName, String lastName, String password) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
