package pl.edu.s28201.webExpenses.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.ExpenseOrigin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseOriginRepository extends CrudRepository<ExpenseOrigin, UUID> {

    Optional<ExpenseOrigin> findByName(String name);

    List<ExpenseOrigin> findByUser(AppUser user);
}
