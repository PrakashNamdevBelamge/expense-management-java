package com.cd.dto;

import java.time.LocalDate;

import com.cd.model.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseRequest {

	private String id;

	@NotNull(message = "UserId is required")
	private String userId;

	@NotBlank(message = "Title is required")
	@Size(max = 250, min = 10, message = "Title length should be in between 10 to 250 chars")
	private String title;

	@NotNull(message = "Amount is required")
	private Double amount;

	@NotNull(message = "Category is required")
	private Category category;

	@PastOrPresent(message = "Expense date should be past or present date")
	private LocalDate date;
}
