package com.manalang.repository;

import com.manalang.entity.CustomerData;
import org.springframework.data.repository.CrudRepository;

public interface CustomerDataRepository extends CrudRepository<CustomerData, Integer> {
    
    CustomerData findByFirstnameAndLastname(String firstname, String lastname);
}

