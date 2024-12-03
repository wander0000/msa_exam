package com.sparta.msa_exam.product.entity;

import com.sparta.msa_exam.product.dto.ProductRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long product_id;
	private String name;
	private Integer supply_price;

	public static Product createProduct(ProductRequestDto requestDto) {
		return Product.builder()
			.name(requestDto.getName())
			.supply_price(requestDto.getSupply_price())
			.build();
	}

}