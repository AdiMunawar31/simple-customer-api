package com.d2y.ecommerce.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d2y.ecommerce.dtos.CustomerDTO;
import com.d2y.ecommerce.dtos.InputCustomerDTO;
import com.d2y.ecommerce.models.CustomerElastic;
import com.d2y.ecommerce.services.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/search")
    public Iterable<CustomerElastic> getProducts() {
        return customerService.getCustomerElasticsearch();
    }

    @GetMapping("/{customerId}")
    public CustomerDTO getCustomerById(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PostMapping
    public CustomerDTO createCustomer(@RequestBody InputCustomerDTO inputCustomerDTO) throws JsonProcessingException {
        return customerService.createCustomer(inputCustomerDTO);
    }

    @PutMapping("/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody InputCustomerDTO inputCustomerDTO) {
        return customerService.updateCustomer(customerId, inputCustomerDTO);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok().body("Customer Deleted Successfully!");
    }
}
