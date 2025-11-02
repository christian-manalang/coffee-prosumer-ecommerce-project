package com.manalang.serviceimpl;

import com.manalang.entity.CustomerData;
import com.manalang.model.Customer;
import com.manalang.repository.CustomerDataRepository;
import com.manalang.service.CustomerService;
import com.manalang.util.Transform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDataRepository customerDataRepository;

    Transform<Customer, CustomerData> transformCustomer = new Transform<>(CustomerData.class);

    @Override
    public CustomerData findOrCreateCustomer(Customer customerModel) {
        log.info("Finding or creating customer: {} {}", customerModel.getFirstname(), customerModel.getLastname());

        CustomerData customerData = customerDataRepository.findByFirstnameAndLastname(
                customerModel.getFirstname(),
                customerModel.getLastname()
        );

        if (customerData == null) {
            log.info("Customer not found. Creating new customer.");
            
            customerData = transformCustomer.transform(customerModel);
            
            customerData = customerDataRepository.save(customerData);
        } else {
            log.info("Found existing customer with ID: {}", customerData.getId());
        }

        return customerData;
    }
}
