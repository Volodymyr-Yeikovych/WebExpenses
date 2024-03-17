package pl.edu.s28201.webExpenses.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.s28201.webExpenses.model.ExpenseCategory;

import java.util.UUID;

public interface ExpenseCategoryRepository extends CrudRepository<ExpenseCategory, UUID> {
}
