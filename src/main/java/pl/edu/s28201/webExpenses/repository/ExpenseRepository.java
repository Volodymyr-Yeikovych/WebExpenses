package pl.edu.s28201.webExpenses.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.expense.Expense;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends CrudRepository<Expense, UUID>, PagingAndSortingRepository<Expense, UUID> {

    @NonNull
    List<Expense> findAll();

    List<Expense> findExpensesByUser(AppUser appUser);

    List<Expense> findExpensesByUser(AppUser appUser, Sort sort);
}
