package pl.edu.s28201.webExpenses.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.expense.Expense;
import pl.edu.s28201.webExpenses.model.expense.ExpenseCategory;
import pl.edu.s28201.webExpenses.repository.ExpenseCategoryRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryService {
    private final UuidService uuidService;
    private final ExpenseCategoryRepository categoryRepository;
    private final SecurityService securityService;
    private final ExpenseRepository expenseRepository;

    @Autowired
    public CategoryService(UuidService uuidService,
                           ExpenseCategoryRepository categoryRepository,
                           SecurityService securityService,
                           ExpenseRepository expenseRepository) {
        this.uuidService = uuidService;
        this.categoryRepository = categoryRepository;
        this.securityService = securityService;
        this.expenseRepository = expenseRepository;
    }

    public void hideAllById(String categories) {
        if (categories != null && !categories.isEmpty()) {
            List<UUID> ids = uuidService.parseExpenseIds(categories, ",");
            StringBuilder deleteMsg = new StringBuilder("Parsed Category IDs to Hide: [");
            for (UUID id : ids) {
                if (isValidCategoryId(id, securityService.getUserFromSecurity())) {
                    categoryRepository.hideById(id);
                    deleteMsg.append(id).append(",");
                }
            }
            deleteMsg.append("]");
            log.info(deleteMsg.toString());
        } else {
            log.info("No categories to hide");
        }
    }

    private boolean isValidCategoryId(UUID id, AppUser currentUser) {
        Optional<ExpenseCategory> catOpt = categoryRepository.findById(id);

        if (catOpt.isEmpty()) return false;

        ExpenseCategory cat = catOpt.get();
        return cat.getUser().equals(currentUser);
    }

    public List<ExpenseCategory> findAll() {
        return categoryRepository.findAllByUserAndNotHidden(securityService.getUserFromSecurity());
    }

    public List<ExpenseCategory> findAllExisting() {
        AppUser currentUser = securityService.getUserFromSecurity();

        List<ExpenseCategory> existingCats = expenseRepository
                .findExpensesByUser(currentUser)
                .stream()
                .map(Expense::getCategory)
                .distinct()
                .collect(Collectors.toList());


        List<ExpenseCategory> allCats = categoryRepository.findByUser(currentUser);
        for (ExpenseCategory cat : allCats) {
            if (!existingCats.contains(cat)) {
                if (cat.isHidden()) {
                    categoryRepository.delete(cat);
                } else {
                    existingCats.add(cat);
                }
            }
        }

        return existingCats;
    }

    public List<ExpenseCategory> parseIdsToCategories(String categories) {
        List<ExpenseCategory> cats = new ArrayList<>();
        if (categories != null && !categories.isEmpty()) {
            List<UUID> ids = uuidService.parseExpenseIds(categories, ",");
            for (UUID id : ids) {
                if (isValidCategoryId(id, securityService.getUserFromSecurity())) {
                    cats.add(categoryRepository.findById(id).get());
                }
            }
        }
        return cats;
    }
}
