package com.d2y.ecommerce.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO implements Serializable {
  private Long customerId;
  private String name;
  private String address;
  private String city;
  private String province;
  private Boolean status;
}
