package com.manalang.service;

import com.manalang.entity.CustomerData;
import com.manalang.model.Customer;

public interface CustomerService {

    CustomerData findOrCreateCustomer(Customer customerModel);
}
