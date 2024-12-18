package com.cd.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cd.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

	Page<Expense> findByUserId(UUID userId, Pageable pageable);

	List<Expense> findByUserIdAndDateGreaterThanEqual(UUID userId, LocalDate date);

	List<Expense> findByUserId(UUID userId);

	List<Expense> findByUserIdAndDateBetween(UUID userId, LocalDate fromDate, LocalDate toDate);

}
