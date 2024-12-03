package com.sparta.msa_exam.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
	private Long orderId;
	private List<Long> orderProductIds;

	public static OrderResponseDto fromEntity(Order order) {
		List<Long> productIds = order.getOrderProducts().stream()
			.map(OrderProduct::getProductId)
			.collect(Collectors.toList());
		return OrderResponseDto.builder()
			.orderId(order.getOrderId())
			.orderProductIds(productIds)
			.build();
	}
}
