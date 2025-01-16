package com.cd.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.cd.commons.PropertiesConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cd.commons.Constants;
import com.cd.dto.ExpenseReportResponse;
import com.cd.dto.ExpenseReportResponse.Report;
import com.cd.dto.ExpenseRequest;
import com.cd.dto.ExpenseResponse;
import com.cd.exceptions.CustomException;
import com.cd.service.ExpenseService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@CrossOrigin
@Tag(name = "Expense related methods", description = "These apis are helpful to fetch add update and  delete expenses")
@RestController
@RequestMapping(value = "api")
public class ExpenseController {

	private final ExpenseService expenseService;
	private final PropertiesConfig propertiesConfig;
	private final Environment environment;

	@Value("${build.number:1.0}")
	private String buildNumber;

	@Value("${profile}")
	private String profile;

	public ExpenseController(ExpenseService expenseService, PropertiesConfig propertiesConfig, Environment environment) {
		this.expenseService = expenseService;
		this.propertiesConfig = propertiesConfig;
		this.environment = environment;
	}

	@Operation(summary = "fetch all expenses", description = "This api is used to fetch all expenses")
	@ApiResponse(description = "Success Response", responseCode = "200")
	@GetMapping(value = "/expenses")
	public ResponseEntity<Page<ExpenseResponse>> getAllExpensesByUserId(
			@PageableDefault(direction = Sort.Direction.ASC, size = 10, page = 0, sort = Constants.CATEGORY_SMALL) Pageable pageable,
			@RequestParam String userId) {
		return ResponseEntity.ok(this.expenseService.getAllExpenses(pageable, UUID.fromString(userId)));
	}

	@GetMapping
	public String getProperty() {
		return this.propertiesConfig.companyName()+ " "+this.propertiesConfig.name()+" "+this.propertiesConfig.umail()+" "+buildNumber+" "+this.environment.getProperty("JAVA_HOME")+profile;
	}

	@GetMapping(value = "/expenses/{id}")
	public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable String id) {
		ExpenseResponse expenseResponse = this.expenseService.getExpenseById(UUID.fromString(id));
		return ResponseEntity.ok(expenseResponse);
	}

	@GetMapping(value = "/expenses/search")
	public ResponseEntity<List<ExpenseResponse>> searchExpenses(@RequestParam String userId,
			@RequestParam(required = false) String searchValue, @RequestParam(required = false) LocalDate fromDate,
			@RequestParam(required = false) LocalDate toDate) {
		return ResponseEntity
				.ok(this.expenseService.searchExpenses(UUID.fromString(userId), searchValue, fromDate, toDate));
	}

	@GetMapping(value = "/expenses/{userId}/{duration}")
	public ResponseEntity<ExpenseReportResponse> getExpenseByMonthly(@PathVariable String userId,
			@PathVariable String duration) {
		ExpenseReportResponse expenseResponse = this.expenseService.getExpenseByType(UUID.fromString(userId), duration);
		return ResponseEntity.ok(expenseResponse);
	}

	@GetMapping(value = "/expenses/download-pdf")
	public void getExpenseInPdf(@RequestParam String duration, @RequestParam String userId,
			HttpServletResponse httpServletResponse) {
		ExpenseReportResponse expenseResponse = this.expenseService.getExpenseByType(UUID.fromString(userId), duration);
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			PdfWriter writer = new PdfWriter(byteArrayOutputStream);
			PdfDocument pdfDocument = new PdfDocument(writer);
			Document document = new Document(pdfDocument);
			document.add(new com.itextpdf.layout.element.Paragraph(Constants.EXPENSE_REPORT + duration).setBold()
					.setFontSize(18).setMarginBottom(10));
			float[] columnWidths = { 2, 4 };
			Table table = new Table(columnWidths);
			table.setWidth(UnitValue.createPercentValue(100));

			table.addHeaderCell(new Cell().add(new Paragraph(Constants.CATEGORY).setBold()));
			table.addHeaderCell(new Cell().add(new Paragraph(Constants.AMOUNT).setBold()));

			for (Report expense : expenseResponse.getReports()) {
				table.addCell(new Cell().add(new Paragraph(expense.getCategory())));
				table.addCell(new Cell().add(new Paragraph(expense.getAmount().toString())));
			}
			table.addCell(new Cell().add(new Paragraph(Constants.TOTAL)));
			table.addCell(new Cell().add(new Paragraph(expenseResponse.getTotal().toString())));
			document.add(table);
			document.close();

			httpServletResponse.setContentType(Constants.CONTENT_TYPE);
			httpServletResponse.setHeader(Constants.CONTENT_DISPOSITION, Constants.ATTACHAMENT_NAME);
			httpServletResponse.getOutputStream().write(byteArrayOutputStream.toByteArray());
			httpServletResponse.getOutputStream().flush();
		} catch (IOException e) {
			throw new CustomException(Constants.PDF_ERROR);
		}
	}

	@PostMapping(value = "/expenses")
	public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
		ExpenseResponse expenseResponse = this.expenseService.addExpense(expenseRequest);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(expenseResponse.getId())
				.toUri();
		return ResponseEntity.created(uri).body(expenseResponse);
	}

	@PutMapping(value = "/expenses/{id}")
	public ResponseEntity<ExpenseResponse> updateExpense(@Valid @RequestBody ExpenseRequest expenseRequest,
			@PathVariable String id) {
		ExpenseResponse expenseResponse = this.expenseService.updateExpense(expenseRequest, UUID.fromString(id));
		return ResponseEntity.ok(expenseResponse);
	}

	@DeleteMapping(value = "/expenses/{id}")
	public ResponseEntity<?> deleteExpense(@PathVariable String id) {
		this.expenseService.deleteExpense(UUID.fromString(id));
		return ResponseEntity.noContent().build();
	}

}
