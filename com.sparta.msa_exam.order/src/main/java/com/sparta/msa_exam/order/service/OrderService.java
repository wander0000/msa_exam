package com.sparta.msa_exam.order.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.msa_exam.order.client.ProductClient;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductClient productClient;
	private final CircuitBreakerRegistry circuitBreakerRegistry;

	@PostConstruct
	public void registerEventListener() {
		circuitBreakerRegistry.circuitBreaker("product").getEventPublisher()
			.onStateTransition(event -> log.info("#######CircuitBreaker State Transition: {}", event)) // 상태 전환 이벤트 리스너
			.onFailureRateExceeded(
				event -> log.info("#######CircuitBreaker Failure Rate Exceeded: {}", event)) // 실패율 초과 이벤트 리스너
			.onCallNotPermitted(
				event -> log.info("#######CircuitBreaker Call Not Permitted: {}", event)) // 호출 차단 이벤트 리스너
			.onError(event -> log.info("#######CircuitBreaker Error: {}", event)); // 오류 발생 이벤트 리스너
	}

	@Transactional
	@CircuitBreaker(name = "product", fallbackMethod = "fallbackOrder")
	public ResponseEntity<?> createOrder(OrderRequestDto requestDto) {
		log.info("#######createOrder 접근");
		for (Long productId : requestDto.getOrderProductIds()) {
			if (productId == 1) {//실패 케이스 임의 설정
				throw new RuntimeException("Product service 호출 실패");
			}
			productClient.getProduct(productId);
		}
		Order order = Order.builder().build();
		log.info("#######order : " + order + "OrderProductIds : " + requestDto.getOrderProductIds());
		order.addProductIds(requestDto.getOrderProductIds());
		return ResponseEntity.ok(OrderResponseDto.fromEntity(orderRepository.save(order)));
	}

	public ResponseEntity<?> fallbackOrder(OrderRequestDto requestDto, Throwable throwable) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("fallback", "잠시 후에 주문 추가를 요청 해주세요.");
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
	}

	public OrderResponseDto getOrderById(Long orderId) {
		return orderRepository.findById(orderId)
			.map(OrderResponseDto::fromEntity)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Transactional
	@CircuitBreaker(name = "product", fallbackMethod = "fallbackUpdateOrder")
	public ResponseEntity<?> updateOrder(Long orderId, Long productId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found or has been deleted"));
		if (productId != null) {
			productClient.getProduct(productId);
		}
		log.info("#######orderId : " + order.getOrderId() + " / OrderProductId : " + productId);
		order.addOrderProduct(productId);
		return ResponseEntity.ok(OrderResponseDto.fromEntity(orderRepository.save(order)));
	}

	public ResponseEntity<?> fallbackUpdateOrder(OrderRequestDto requestDto, Throwable throwable) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("fallback", "잠시 후에 상품 추가를 요청 해주세요.");
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
	}
}