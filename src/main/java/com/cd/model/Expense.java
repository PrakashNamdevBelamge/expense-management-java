package com.cd.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Expense {

	@Id
	@GeneratedValue()
	private UUID id;
	
	//This is to store expense with user
	@Column(name = "user_id")
	private UUID userId;
	
	@Column(name = "title",length = 100)
	private String title;
	
	@Column(name = "amount",length = 50)
	private Double amount;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "category",length = 50)
	private Category category;
	
	@Column(name = "date")
	private LocalDate date;
}
