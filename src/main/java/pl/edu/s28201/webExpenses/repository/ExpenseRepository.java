package pl.edu.s28201.webExpenses.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.Expense;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends CrudRepository<Expense, UUID> {

    @NonNull
    List<Expense> findAll();

    List<Expense> findExpensesByUser(AppUser appUser);

}
