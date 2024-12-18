package com.cd.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cd.commons.Constants;
import com.cd.dto.ExpenseReportResponse;
import com.cd.dto.ExpenseReportResponse.Report;
import com.cd.dto.ExpenseRequest;
import com.cd.dto.ExpenseResponse;
import com.cd.exceptions.CustomException;
import com.cd.model.Category;
import com.cd.model.Expense;
import com.cd.repository.ExpenseRepository;
import com.cd.service.ExpenseService;

@Service
public class ExpenseServiceImplementation implements ExpenseService {

	private final ExpenseRepository expenseRepository;

	private final ModelMapper modelMapper;

	public ExpenseServiceImplementation(ExpenseRepository expenseRepository, ModelMapper modelMapper) {
		this.expenseRepository = expenseRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Page<ExpenseResponse> getAllExpenses(Pageable pageable, UUID userId) {
		Page<Expense> pages = this.expenseRepository.findByUserId(userId, pageable);
		List<ExpenseResponse> expenses = pages.getContent().stream()
				.map(expense -> modelMapper.map(expense, ExpenseResponse.class)).collect(Collectors.toList());
		return new PageImpl<>(expenses, pages.getPageable(), pages.getTotalElements());
	}

	@Override
	public ExpenseResponse getExpenseById(UUID id) {
		Expense expense = this.expenseRepository.findById(id)
				.orElseThrow(() -> new CustomException(Constants.EXPENSE_NOT_EXIST));
		ExpenseResponse expenseResponse = modelMapper.map(expense, ExpenseResponse.class);
		return expenseResponse;
	}

	@Override
	public ExpenseReportResponse getExpenseByType(UUID userId, String duration) {
		List<Expense> expenses = null;
		if (duration.equalsIgnoreCase(Constants.CURRENT)) {
			LocalDate local = LocalDate.now();
			local = local.withDayOfMonth(1);
			expenses = this.expenseRepository.findByUserIdAndDateGreaterThanEqual(userId, local);
		} else if (duration.equalsIgnoreCase(Constants.SIX_MONTH)) {
			LocalDate local = LocalDate.now();
			local = local.minusMonths(6);
			expenses = this.expenseRepository.findByUserIdAndDateGreaterThanEqual(userId, local);
		} else if (duration.equalsIgnoreCase(Constants.YEAR)) {
			LocalDate local = LocalDate.now();
			local = local.minusYears(1);
			expenses = this.expenseRepository.findByUserIdAndDateGreaterThanEqual(userId, local);
		}
		Map<Category, List<Expense>> group = expenses.stream().collect(Collectors.groupingBy(Expense::getCategory));
		ExpenseReportResponse expenseReportResponse = new ExpenseReportResponse();
		expenseReportResponse.setReports(new ArrayList<>());
		Double total = 0d;
		for (Entry<Category, List<Expense>> data : group.entrySet()) {
			Report report = new Report();
			Double sum = data.getValue().stream().map(exp -> exp.getAmount()).reduce(0d, (a, b) -> a + b);
			report.setCategory(data.getKey().toString());
			report.setAmount(sum);
			total = total + sum;
			expenseReportResponse.getReports().add(report);
		}
		expenseReportResponse.setTotal(total);
		return expenseReportResponse;
	}

	@Override
	public List<ExpenseResponse> searchExpenses(UUID userId, String searchValue, LocalDate fromDate, LocalDate toDate) {
		List<ExpenseResponse> expenseResponses = null;
		List<Expense> expenses = null;
		if (Objects.nonNull(searchValue)) {
			expenses = this.expenseRepository.findByUserId(userId);

			Pattern pattern = Pattern.compile(".*" + Pattern.quote(searchValue) + ".*", Pattern.CASE_INSENSITIVE);

			expenseResponses = expenses.stream().filter(emp -> {
				return pattern.matcher(emp.getTitle()).matches()
						|| pattern.matcher(emp.getAmount().toString()).matches()
						|| pattern.matcher(emp.getCategory().name()).matches()
						|| pattern.matcher(emp.getDate().toString()).matches();
			}).map(data -> modelMapper.map(data, ExpenseResponse.class)).collect(Collectors.toList());

		} else if (Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
			expenses = this.expenseRepository.findByUserIdAndDateBetween(userId, fromDate, toDate);
			expenseResponses = expenses.stream().map(data -> modelMapper.map(data, ExpenseResponse.class))
					.collect(Collectors.toList());
		}
		return expenseResponses;
	}

	@Override
	public ExpenseResponse addExpense(ExpenseRequest expenseRequestDTO) {
		Expense expense = modelMapper.map(expenseRequestDTO, Expense.class);
		expense = this.expenseRepository.save(expense);
		ExpenseResponse expenseResponse = modelMapper.map(expense, ExpenseResponse.class);
		return expenseResponse;
	}

	@Override
	public ExpenseResponse updateExpense(ExpenseRequest expenseRequestDTO, UUID id) {
		Expense expense = this.findExpenseById(id);
		modelMapper.map(expenseRequestDTO, expense);
		expense.setId(id);
		this.expenseRepository.save(expense);
		ExpenseResponse expenseResponse = modelMapper.map(expense, ExpenseResponse.class);
		return expenseResponse;
	}

	@Override
	public boolean deleteExpense(UUID id) {
		this.findExpenseById(id);
		this.expenseRepository.deleteById(id);
		return true;
	}

	private Expense findExpenseById(UUID id) {
		return this.expenseRepository.findById(id).orElseThrow(() -> new CustomException(Constants.EXPENSE_NOT_EXIST));
	}

}
