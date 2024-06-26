package pl.edu.s28201.webExpenses.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import pl.edu.s28201.webExpenses.model.AppUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends CrudRepository<AppUser, UUID> {

    Optional<AppUser> findByEmail(String email);

    List<AppUser> findByLastName(String lastName);

    @NonNull
    List<AppUser> findAll();

}
