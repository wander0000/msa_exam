package com.sparta.msa_exam.order.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderProduct> orderProducts = new ArrayList<>();

	public void addProductIds(List<Long> productIds) {
		if (this.orderProducts == null) {
			this.orderProducts = new ArrayList<>();
		}
		for (Long productId : productIds) {
			this.orderProducts.add(OrderProduct.builder()
				.order(this)
				.productId(productId)
				.build());
		}
	}

	public void addOrderProduct(Long productId) {
		this.orderProducts.add(OrderProduct.builder()
			.order(this)
			.productId(productId)
			.build());
	}
}

