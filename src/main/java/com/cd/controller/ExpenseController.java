package com.cd.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cd.dto.ExpenseReportResponse;
import com.cd.dto.ExpenseRequest;
import com.cd.dto.ExpenseResponse;
import com.cd.service.ExpenseService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value="api")
public class ExpenseController {
	
	private final ExpenseService expenseService;

	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	@GetMapping(value="/expenses")
	public ResponseEntity<Page<ExpenseResponse>> getAllExpensesByUserId(
			@PageableDefault(direction = Sort.Direction.ASC, size = 10, page = 0, sort = "category") Pageable pageable, @RequestParam String userId) {
		return ResponseEntity.ok(this.expenseService.getAllExpenses(pageable, UUID.fromString(userId)));
	}

	@GetMapping(value = "/expenses/{id}")
	public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable String id) {
		ExpenseResponse expenseResponse = this.expenseService.getExpenseById(UUID.fromString(id));
		return ResponseEntity.ok(expenseResponse);
	}
	
	@GetMapping(value = "/expenses/search")
	public ResponseEntity<List<ExpenseResponse>> searchExpenses(@RequestParam String userId, @RequestParam(required = false) String searchValue,@RequestParam(required = false) LocalDate fromDate,@RequestParam(required = false) LocalDate toDate) {
		return ResponseEntity.ok(this.expenseService.searchExpenses(UUID.fromString(userId),searchValue, fromDate, toDate));
	}
	
	@GetMapping(value = "/expenses/{userId}/{type}")
	public ResponseEntity<ExpenseReportResponse> getExpenseByMonthly(@PathVariable String userId, @PathVariable String type) {
		ExpenseReportResponse expenseResponse = this.expenseService.getExpenseByType(UUID.fromString(userId), type);
		return ResponseEntity.ok(expenseResponse);
	}

	@PostMapping(value="/expenses")
	public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
		ExpenseResponse expenseResponse = this.expenseService.addExpense(expenseRequest);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(expenseResponse.getId())
				.toUri();
		return ResponseEntity.created(uri).body(expenseResponse);
	}

	@PutMapping(value = "/expenses/{id}")
	public ResponseEntity<ExpenseResponse> updateExpense(@Valid @RequestBody ExpenseRequest expenseRequest, @PathVariable String id) {
		ExpenseResponse expenseResponse = this.expenseService.updateExpense(expenseRequest, UUID.fromString(id));
		return ResponseEntity.ok(expenseResponse);
	}

	@DeleteMapping(value = "/expenses/{id}")
	public ResponseEntity<?> deleteExpense(@PathVariable String id) {
		this.expenseService.deleteExpense(UUID.fromString(id));
		return ResponseEntity.noContent().build();
	}

}
