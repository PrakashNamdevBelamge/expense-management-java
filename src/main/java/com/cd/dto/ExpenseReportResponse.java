package com.cd.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseReportResponse {

	List<Report> reports;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class Report{
		private String category;
		private Double amount;
	}
}
