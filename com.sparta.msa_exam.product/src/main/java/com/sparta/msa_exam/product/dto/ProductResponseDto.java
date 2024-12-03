package com.sparta.msa_exam.product.dto;

import java.io.Serializable;

import com.sparta.msa_exam.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto implements Serializable {
	private Long product_id;
	private String name;
	private Integer supply_price;

	public static ProductResponseDto fromEntity(Product product) {
		return ProductResponseDto.builder()
			.product_id(product.getProduct_id())
			.name(product.getName())
			.supply_price(product.getSupply_price())
			.build();
	}
}