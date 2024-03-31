package pl.edu.s28201.webExpenses.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.expense.ExpenseCategory;
import pl.edu.s28201.webExpenses.model.expense.ExpenseShop;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public interface ExpenseCategoryRepository extends CrudRepository<ExpenseCategory, UUID> {

    Optional<ExpenseCategory> findByName(String name);

    List<ExpenseCategory> findAllByName(String name);

    Optional<ExpenseCategory> findByNameIgnoreCaseAndUser(String name, AppUser user);

    List<ExpenseCategory> findByUser(AppUser user);

    default List<ExpenseCategory> findAllByUserAndNotHidden(AppUser user) {
        return findByUser(user).stream().filter(cat -> !cat.isHidden()).collect(Collectors.toList());
    }

    @Modifying
    @Transactional
    default void hideAllById(List<UUID> ids) {
        for (UUID id : ids) hideById(id);
    }

    @Modifying
    @Query("UPDATE ExpenseCategory c SET c.isHidden = true WHERE c.id = ?1")
    @Transactional
    void hideById(UUID id);

    @Modifying
    @Query("UPDATE ExpenseCategory c SET c.isHidden = false WHERE c.id = ?1")
    @Transactional
    default void showById(UUID id) {
        findById(id).ifPresent(category -> category.setHidden(false));
    }
}
