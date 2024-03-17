package pl.edu.s28201.webExpenses.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.s28201.webExpenses.model.ExpenseOrigin;

import java.util.UUID;

public interface ExpenseOriginRepository extends CrudRepository<ExpenseOrigin, UUID> {
}
