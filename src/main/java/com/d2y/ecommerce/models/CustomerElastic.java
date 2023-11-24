package com.d2y.ecommerce.models;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "customer")
public class CustomerElastic {
  private Long id;
  private String name;
  private String address;
  private String city;
  private String province;
  private Boolean status;
}
