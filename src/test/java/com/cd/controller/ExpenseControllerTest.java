package com.cd.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cd.dto.ExpenseReportResponse;
import com.cd.dto.ExpenseReportResponse.Report;
import com.cd.dto.ExpenseRequest;
import com.cd.dto.ExpenseResponse;
import com.cd.model.Category;
import com.cd.model.Expense;
import com.cd.service.ExpenseService;

@ExtendWith(MockitoExtension.class)
public class ExpenseControllerTest {

	@Mock
	@Autowired
	private ExpenseService expenseService;

	@InjectMocks
	private ExpenseController expenseController;

	private Expense expense;
	private UUID testId;
	private UUID testUserId;
	private ExpenseResponse testResponse;
	private ExpenseRequest testRequest;

	@BeforeEach
	public void setup() {
		testId = UUID.randomUUID();
		testUserId = UUID.randomUUID();
		expense = new Expense();
		expense.setId(testId);
		expense.setTitle("I had lunch in company canteen");
		expense.setAmount(250d);
		expense.setCategory(Category.FOOD);
		expense.setId(testId);
		expense.setUserId(testUserId);
		expense.setDate(LocalDate.now());

		testResponse = new ExpenseResponse();
		testResponse.setAmount(expense.getAmount());
		testResponse.setUserId(expense.getUserId().toString());
		testResponse.setId(expense.getId().toString());
		testResponse.setTitle(expense.getTitle());
		testResponse.setDate(expense.getDate());
		testResponse.setCategory(expense.getCategory());

		testRequest = new ExpenseRequest();
		testRequest.setAmount(expense.getAmount());
		testRequest.setUserId(expense.getUserId().toString());
		testRequest.setId(expense.getId().toString());
		testRequest.setTitle(expense.getTitle());
		testRequest.setDate(expense.getDate());
		testRequest.setCategory(expense.getCategory());
	}

	@Test
	public void getAllExpensesByUserId() {
		Page<ExpenseResponse> mockEmps = new PageImpl<>(Collections.singletonList(testResponse));
		Pageable page = PageRequest.of(0, 10);
		when(expenseService.getAllExpenses(page, testUserId)).thenReturn(mockEmps);

		ResponseEntity<Page<ExpenseResponse>> response = expenseController.getAllExpensesByUserId(PageRequest.of(0, 10),
				testUserId.toString());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, Objects.requireNonNull(response.getBody().getContent()).size());
		assertEquals(testResponse, response.getBody().getContent().get(0));
		verify(expenseService).getAllExpenses(page, testUserId);
	}

	@Test
	    public void getExpenseById() {
	        when(expenseService.getExpenseById(testId)).thenReturn(testResponse);
	        ResponseEntity<ExpenseResponse> response = expenseController.getExpenseById(testId.toString());
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(testResponse, response.getBody());
	        verify(expenseService).getExpenseById(testId);
	    }
	
	@Test
    public void searchEmployees() {
        when(expenseService.searchExpenses(testUserId,"FOOD",null, null)).thenReturn(Collections.singletonList(testResponse));
        ResponseEntity<List<ExpenseResponse>> response = expenseController.searchExpenses(testUserId.toString(), "FOOD", null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testResponse, response.getBody().get(0));
        verify(expenseService).searchExpenses(testUserId,"FOOD",null, null);
    }
	
	@Test
    public void getExpenseByMonthly() {
		ExpenseReportResponse expenseReport = new ExpenseReportResponse();
		Report report = new Report(Category.FOOD.name(),120d);
		expenseReport.setReports(new ArrayList<>());
		expenseReport.getReports().add(report);
        when(expenseService.getExpenseByType(testUserId,"current")).thenReturn(expenseReport);
        ResponseEntity<ExpenseReportResponse> response = expenseController.getExpenseByMonthly(testUserId.toString(), "current");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expenseReport, response.getBody());
        verify(expenseService).getExpenseByType(testUserId,"current");
    }

	@Test
	public void addExpense() throws URISyntaxException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		when(expenseService.addExpense(any(ExpenseRequest.class))).thenReturn(testResponse);
		ResponseEntity<ExpenseResponse> response = expenseController.addExpense(testRequest);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(testResponse, response.getBody());
		verify(expenseService).addExpense(testRequest);
	}

	@Test
	    public void updateExpense() {
	        when(expenseService.updateExpense(testRequest, testId)).thenReturn(testResponse);
	        ResponseEntity<ExpenseResponse> updatedExpense = expenseController.updateExpense(testRequest, testId.toString());
	        assertEquals(testResponse, updatedExpense.getBody());
	        verify(expenseService ).updateExpense(testRequest, testId);
	    }

	@Test
	    public void deleteExpense() {
	        when(expenseService.deleteExpense(testId)).thenReturn(true);

	        ResponseEntity<?> response = expenseController.deleteExpense(testId.toString());
	        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	        verify(expenseService).deleteExpense(testId);
	    }

}
