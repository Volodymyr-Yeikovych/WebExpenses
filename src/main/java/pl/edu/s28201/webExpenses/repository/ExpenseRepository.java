package pl.edu.s28201.webExpenses.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.s28201.webExpenses.model.Expense;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, UUID> {

    List<Expense> findAll();
}
