package com.sparta.msa_exam.product.service;

import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@CachePut(cacheNames = "ProductCache", key = "#result.product_id")
	public ProductResponseDto createProduct(ProductRequestDto requestDto) {
		Product product = Product.createProduct(requestDto);
		return ProductResponseDto.fromEntity(productRepository.save(product));
	}

	@Cacheable(cacheNames = "ProductAllCache", key = "methodName")
	public List<ProductResponseDto> getProducts() {
		return productRepository.findAll()
			.stream()
			.map(ProductResponseDto::fromEntity)
			.toList();
	}

	@Cacheable(cacheNames = "ProductCache", key = "args[0]")
	public ProductResponseDto getProductById(Long productId) {
		return productRepository.findById(productId)
			.map(ProductResponseDto::fromEntity)
			.orElseThrow(() ->
				new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

}