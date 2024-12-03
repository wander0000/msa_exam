package com.sparta.msa_exam.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sparta.msa_exam.order.dto.ProductResponseDto;

@FeignClient(name = "product")
public interface ProductClient {
	@GetMapping("/products/{productId}")
	ProductResponseDto getProduct(@PathVariable Long productId);

}
