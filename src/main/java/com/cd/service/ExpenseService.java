package com.cd.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cd.dto.ExpenseReportResponse;
import com.cd.dto.ExpenseRequest;
import com.cd.dto.ExpenseResponse;

public interface ExpenseService {

	public Page<ExpenseResponse> getAllExpenses(Pageable pageable, UUID userId);

	public ExpenseResponse getExpenseById(UUID id);

	public ExpenseReportResponse getExpenseByType(UUID userId, String duration);

	public List<ExpenseResponse> searchExpenses(UUID userId, String searchValue, LocalDate fromDate, LocalDate toDate);

	public ExpenseResponse addExpense(ExpenseRequest expenseRequestDTO);

	public ExpenseResponse updateExpense(ExpenseRequest expenseRequestDTO, UUID id);

	public boolean deleteExpense(UUID id);

}
