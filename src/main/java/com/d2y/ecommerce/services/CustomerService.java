package com.d2y.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.d2y.ecommerce.dtos.CustomerDTO;
import com.d2y.ecommerce.dtos.InputCustomerDTO;
import com.d2y.ecommerce.models.Customer;
import com.d2y.ecommerce.models.CustomerElastic;
import com.d2y.ecommerce.repositories.CustomerElasticRepository;
import com.d2y.ecommerce.repositories.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@CacheConfig(cacheNames = { "customers" })
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerElasticRepository customerElasticRepository;
  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Cacheable
  public List<CustomerDTO> getAllCustomers() {
    List<Customer> customers = customerRepository.findAll();
    return customers.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public Iterable<CustomerElastic> getCustomerElasticsearch() {
    return customerElasticRepository.findAll();
  }

  @Cacheable(key = "#customerId")
  public CustomerDTO getCustomerById(Long customerId) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
    return convertToDTO(customer);
  }

  public CustomerDTO createCustomer(InputCustomerDTO inputCustomerDTO) throws JsonProcessingException {
    Customer newCustomer = convertToEntity(inputCustomerDTO);
    Customer savedCustomer = customerRepository.save(newCustomer);

    CustomerElastic customerElastic = convertToElasticEntity(savedCustomer);
    customerElasticRepository.save(customerElastic);

    String jsonNewCustomer = objectMapper.writeValueAsString(savedCustomer);
    kafkaTemplate.send("customer-topic", jsonNewCustomer);

    return convertToDTO(savedCustomer);
  }

  public CustomerDTO updateCustomer(Long customerId, InputCustomerDTO inputCustomerDTO) {
    Customer existingCustomer = customerRepository.findById(customerId)
        .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
    CustomerElastic existingCustomerElastic = customerElasticRepository.findById(customerId)
        .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

    existingCustomer.setName(inputCustomerDTO.getName());
    existingCustomer.setAddress(inputCustomerDTO.getAddress());
    existingCustomer.setCity(inputCustomerDTO.getCity());
    existingCustomer.setProvince(inputCustomerDTO.getProvince());
    existingCustomer.setStatus(inputCustomerDTO.getStatus());

    existingCustomerElastic.setName(inputCustomerDTO.getName());
    existingCustomerElastic.setAddress(inputCustomerDTO.getAddress());
    existingCustomerElastic.setCity(inputCustomerDTO.getCity());
    existingCustomerElastic.setProvince(inputCustomerDTO.getProvince());
    existingCustomerElastic.setStatus(inputCustomerDTO.getStatus());

    Customer updatedCustomer = customerRepository.save(existingCustomer);
    customerElasticRepository.save(existingCustomerElastic);

    return convertToDTO(updatedCustomer);
  }

  public void deleteCustomer(Long customerId) {
    customerRepository.deleteById(customerId);
    customerElasticRepository.deleteById(customerId);
  }

  private CustomerElastic convertToElasticEntity(Customer customer) {
    return CustomerElastic.builder()
        .id(customer.getCustomerId())
        .name(customer.getName())
        .address(customer.getAddress())
        .city(customer.getCity())
        .province(customer.getProvince())
        .status(customer.getStatus())
        .build();
  }

  private CustomerDTO convertToDTO(Customer customer) {
    return CustomerDTO.builder()
        .customerId(customer.getCustomerId())
        .name(customer.getName())
        .address(customer.getAddress())
        .city(customer.getCity())
        .province(customer.getProvince())
        .status(customer.getStatus())
        .build();
  }

  private Customer convertToEntity(InputCustomerDTO inputCustomerDTO) {
    return Customer.builder()
        .name(inputCustomerDTO.getName())
        .address(inputCustomerDTO.getAddress())
        .city(inputCustomerDTO.getCity())
        .province(inputCustomerDTO.getProvince())
        .status(inputCustomerDTO.getStatus())
        .build();
  }

}
