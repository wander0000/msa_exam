package com.sparta.msa_exam.order.dto;

import java.util.List;

import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchDto {
	private List<Long> orderItemIds;
	private String sortBy;
	private Pageable pageable;
}