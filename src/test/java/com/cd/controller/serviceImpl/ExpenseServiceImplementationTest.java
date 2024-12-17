package com.cd.controller.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.cd.dto.ExpenseRequest;
import com.cd.dto.ExpenseResponse;
import com.cd.exceptions.CustomException;
import com.cd.model.Category;
import com.cd.model.Expense;
import com.cd.repository.ExpenseRepository;
import com.cd.serviceImpl.ExpenseServiceImplementation;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplementationTest {
	@Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ExpenseServiceImplementation expenseServiceImplementation;

    private UUID userId = UUID.randomUUID();;
    private Expense expense;
    private Page<Expense> expensePage;
    private Pageable pageable;
	private UUID testId = UUID.randomUUID();;
	private ExpenseResponse testResponse;
	private ExpenseRequest testRequest;
	
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);


		expense = new Expense();
		expense.setId(testId);
		expense.setTitle("I had lunch in company canteen");
		expense.setAmount(250d);
		expense.setCategory(Category.FOOD);
		expense.setId(testId);
		expense.setUserId(userId);
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

        pageable = PageRequest.of(0, 5);  // Set pagination, page size = 5, page number = 0
        expensePage = new PageImpl<>(List.of(expense));  // A page of 1 expense
    }

    @Test
    public void testGetAllExpenses() {
        when(expenseRepository.findByUserId(userId, pageable)).thenReturn(expensePage);
        when(modelMapper.map(expense, ExpenseResponse.class)).thenReturn(testResponse);
        Page<ExpenseResponse> result = expenseServiceImplementation.getAllExpenses(pageable, userId);
        assertNotNull(result);
        assertEquals(1, result.getContent().size()); 
        assertEquals(testResponse.getTitle(), result.getContent().get(0).getTitle());  
    }

    @Test
    public void testGetExpenseById() {
        when(expenseRepository.findById(any(UUID.class))).thenReturn(Optional.of(expense));
        when(modelMapper.map(expense, ExpenseResponse.class)).thenReturn(testResponse);
        ExpenseResponse result = expenseServiceImplementation.getExpenseById(expense.getId());
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());  
        assertEquals("I had lunch in company canteen", result.getTitle());  
    }

    @Test
    public void testAddExpense() {
    	
        when(expenseRepository.save(expense)).thenReturn(expense);

        System.out.println("Before method execution");
        System.out.println("Test Request: " + testRequest);
        System.out.println("Mapped Expense: " + expense);
        System.out.println("Expected Response: " + testResponse);
        // Call the method to test
        ExpenseResponse result = expenseServiceImplementation.addExpense(testRequest);
        System.out.println("Request DTO: " + testRequest);
        System.out.println("Mapped Expense: " + testResponse);
        System.out.println("Saved Expense: " + expense);
        System.out.println("Expected Response: " + result);
        // Assertions
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        assertEquals(testResponse.getAmount(), result.getAmount());
    	
    }


    @Test
    public void testUpdateExpense() {

    	
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        when(modelMapper.map(testRequest, Expense.class)).thenReturn(expense);
        when(expenseRepository.save(expense)).thenReturn(expense);
        when(modelMapper.map(expense, ExpenseResponse.class)).thenReturn(testResponse);

        ExpenseResponse result = expenseServiceImplementation.updateExpense(testRequest, testId);

        assertNotNull(result);
        assertEquals(Category.FOOD, result.getCategory());  
        assertEquals(250d, result.getAmount());  
    }

    @Test
    public void testDeleteExpense() {
        boolean result = expenseServiceImplementation.deleteExpense(testId);
        assertTrue(result);  
        verify(expenseRepository).deleteById(testId);  
    }

    @Test
    public void testDeleteExpense_NotFound() {
        when(expenseRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Exception exception = assertThrows(CustomException.class, () -> {
            expenseServiceImplementation.deleteExpense(UUID.randomUUID());
        });
        assertEquals("Expense record not exists", exception.getMessage());
    }

}
