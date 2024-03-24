package pl.edu.s28201.webExpenses.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.expense.ExpenseCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseCategoryRepository extends CrudRepository<ExpenseCategory, UUID> {

    Optional<ExpenseCategory> findByName(String name);

    List<ExpenseCategory> findByUser(AppUser user);
}
