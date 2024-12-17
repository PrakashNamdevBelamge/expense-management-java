//package com.cd.controller.serviceImpl;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.mockito.BDDMockito.given;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import com.cd.dto.ExpenseRequest;
//import com.cd.dto.ExpenseResponse;
//import com.cd.exceptions.CustomException;
//import com.cd.model.Category;
//import com.cd.model.Expense;
//import com.cd.repository.ExpenseRepository;
//import com.cd.service.ExpenseService;
//import com.cd.serviceImpl.ExpenseServiceImplementation;
//
//@ExtendWith(MockitoExtension.class)
//public class ExpenseServiceImplementationTest {
//	@Mock
//    private ExpenseRepository expenseRepository;
//
//	@Mock
//    private ExpenseService expenseService;
//
//
//    @InjectMocks
//    private ExpenseServiceImplementation expenseServiceImplementation;
//
//    private UUID userId ;
//    private Expense expense;
//    private UUID testId;
//    private Page<Expense> expensePage;
//    private Pageable pageable;
//	private ExpenseResponse testResponse;
//	private ExpenseRequest testRequest;
//	
//    @BeforeEach
//    public void setUp() {
//    	 MockitoAnnotations.openMocks(this);
//    	testId = UUID.randomUUID();;
//    	userId = UUID.randomUUID();;
//
//		expense = new Expense();
//		expense.setTitle("I had lunch in company canteen");
//		expense.setAmount(250d);
//		expense.setCategory(Category.FOOD);
//		expense.setId(testId);
//		expense.setUserId(userId);
//		expense.setDate(LocalDate.now());
//
//		testResponse = new ExpenseResponse();
//		testResponse.setAmount(expense.getAmount());
//		testResponse.setUserId(expense.getUserId().toString());
//		testResponse.setId(testId.toString());
//		testResponse.setTitle(expense.getTitle());
//		testResponse.setDate(expense.getDate());
//		testResponse.setCategory(expense.getCategory());
//
//		testRequest = new ExpenseRequest();
//		testRequest.setAmount(expense.getAmount());
//		testRequest.setUserId(expense.getUserId().toString());
//		testRequest.setId(testId.toString());
//		testRequest.setTitle(expense.getTitle());
//		testRequest.setDate(expense.getDate());
//		testRequest.setCategory(expense.getCategory());
//
//        pageable = PageRequest.of(0, 5);  // Set pagination, page size = 5, page number = 0
//        expensePage = new PageImpl<>(List.of(expense));  // A page of 1 expense
//        when(expenseRepository.findById(testId)).thenReturn(Optional.of(expense));  // Return mock expense for testId
//
//    }
//
//    @Test
//    public void testGetAllExpenses() {
//        when(expenseRepository.findByUserId(userId, pageable)).thenReturn(expensePage);
//        Page<ExpenseResponse> result = expenseServiceImplementation.getAllExpenses(pageable, userId);
//        assertNotNull(result);
//        assertEquals(1, result.getContent().size()); 
//        assertEquals(testResponse.getTitle(), result.getContent().get(0).getTitle());  
//    }
//
//    @Test
//    public void testGetExpenseById() {
//        when(expenseRepository.findById(testId)).thenReturn(Optional.of(expense));
//        ExpenseResponse result = expenseService.getExpenseById(testId);
//        assertNotNull(result);
//        assertEquals(testResponse.getId(), result.getId());  
//        assertEquals("I had lunch in company canteen", result.getTitle());  
//    }
//    
//    @Test
//    public void testGetExpenseById_ExpenseNotFound() {
//      when(expenseRepository.findById(testId)).thenReturn(Optional.empty());
//
//      CustomException exception = assertThrows(CustomException.class, () -> {
//    	  expenseServiceImplementation.getExpenseById(testId);
//      });
//      assertEquals("Expense record not exists", exception.getMessage());
//
//      verify(expenseRepository).findById(testId);
//    }
//
//    @Test
//    public void testAddExpense() {
//    	
//        given(expenseRepository.save(expense)).willReturn(expense);
//       
//        ExpenseResponse result = expenseServiceImplementation.addExpense(testRequest);
//       
//        assertNotNull(result);
//        assertEquals(testResponse.getId(), result.getId());
//        assertEquals(testResponse.getAmount(), result.getAmount());
//    	
//    }
//
//
//    @Test
//    public void testUpdateExpense() {
//
//    	
//        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
//        when(expenseRepository.save(expense)).thenReturn(expense);
//
//        ExpenseResponse result = expenseServiceImplementation.updateExpense(testRequest, testId);
//
//        assertNotNull(result);
//        assertEquals(Category.FOOD, result.getCategory());  
//        assertEquals(250d, result.getAmount());  
//    }
//
//    @Test
//    public void testDeleteExpense() {
//        boolean result = expenseServiceImplementation.deleteExpense(testId);
//        assertTrue(result);  
//        verify(expenseRepository).deleteById(testId);  
//    }
//
//    @Test
//    public void testDeleteExpense_NotFound() {
//        when(expenseRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
//        Exception exception = assertThrows(CustomException.class, () -> {
//            expenseServiceImplementation.deleteExpense(UUID.randomUUID());
//        });
//        assertEquals("Expense record not exists", exception.getMessage());
//    }
//
//}
