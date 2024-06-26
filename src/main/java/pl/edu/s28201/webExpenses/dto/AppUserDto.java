package pl.edu.s28201.webExpenses.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 254, message = "First name cannot be more than 254 characters")
    private String firstName;
    @NotNull
    @Size(max = 254, message = "Last name cannot be more than 254 characters")
    private String lastName;
    @NotNull
    @NotEmpty
    @Size(min = 6, message = "Password Must be at least 6 characters long.")
    private String password;
    @NotNull
    @NotEmpty
    @Size(min = 6, message = "Password Must be at least 6 characters long.")
    private String matchingPassword;

    public AppUserDto(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
