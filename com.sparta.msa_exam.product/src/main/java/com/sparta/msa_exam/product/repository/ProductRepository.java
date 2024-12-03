package com.sparta.msa_exam.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.msa_exam.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
