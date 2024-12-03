package com.sparta.msa_exam.order.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.dto.ProductRequestDto;
import com.sparta.msa_exam.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
		return ResponseEntity.ok(orderService.createOrder(orderRequestDto));
	}

	@GetMapping("/{orderId}")
	public OrderResponseDto getOrderById(@PathVariable Long orderId) {
		return orderService.getOrderById(orderId);
	}

	@PutMapping("/{orderId}")
	public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody ProductRequestDto requestDto) {
		return ResponseEntity.ok(orderService.updateOrder(orderId, requestDto.getProductId()));
	}
}
