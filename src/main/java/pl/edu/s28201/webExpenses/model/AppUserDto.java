package pl.edu.s28201.webExpenses.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.s28201.webExpenses.validation.email.EmailValidation;
import pl.edu.s28201.webExpenses.validation.password.MatchingPasswords;

@Data
@NoArgsConstructor
@MatchingPasswords
@Builder
@AllArgsConstructor
public class AppUserDto {

    @NotNull
    @NotEmpty
    @EmailValidation
    private String email;
    @NotNull
    @NotEmpty
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String matchingPassword;

    public AppUserDto(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
