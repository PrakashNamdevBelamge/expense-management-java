package com.cd.dto;

import java.time.LocalDate;

import com.cd.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseResponse {

	private String id;

	private String userId;

	private String title;

	private Double amount;

	private Category category;

	private LocalDate date;
}
