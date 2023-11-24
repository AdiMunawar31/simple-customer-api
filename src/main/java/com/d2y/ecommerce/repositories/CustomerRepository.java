package com.d2y.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d2y.ecommerce.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
