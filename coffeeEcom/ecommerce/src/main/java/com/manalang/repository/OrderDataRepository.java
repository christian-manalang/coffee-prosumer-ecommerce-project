package com.manalang.repository;

import com.manalang.entity.OrderData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDataRepository extends CrudRepository<OrderData, Integer> {

    List<OrderData> findByCustomer_Id(Integer customerId);
}

