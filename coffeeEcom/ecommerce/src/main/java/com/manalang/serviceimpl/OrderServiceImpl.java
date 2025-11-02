package com.manalang.serviceimpl;

import com.manalang.entity.CustomerData;
import com.manalang.entity.OrderData;
import com.manalang.entity.OrderItemData;
import com.manalang.entity.ProductData;
import com.manalang.model.Order;
import com.manalang.model.OrderItem;
import com.manalang.repository.OrderDataRepository;
import com.manalang.repository.ProductDataRepository; 
import com.manalang.service.CustomerService;
import com.manalang.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDataRepository orderDataRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductDataRepository productDataRepository;

    @Override
    @Transactional
    public Order create(Order orderModel) {
        log.info("--- Creating new order ---");

        CustomerData customerData = customerService.findOrCreateCustomer(orderModel.getCustomer());

        OrderData orderData = new OrderData();
        orderData.setCustomer(customerData);
        orderData.setTotalPrice(BigDecimal.valueOf(orderModel.getTotalPrice()));
        orderData.setTotalQuantity(orderModel.getTotalQuantity());
        orderData.setStatus("PROCESSING"); 
        orderData.setDateCreated(new Date());
        log.info("Creating OrderData header for customer ID: {}", customerData.getId());

        Set<OrderItemData> orderItems = new HashSet<>();
        
        for (OrderItem itemModel : orderModel.getItems()) {

            ProductData product = productDataRepository.findById(itemModel.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemModel.getProductId()));

            OrderItemData itemData = new OrderItemData();
            itemData.setProductId(product.getId());
            itemData.setProductName(product.getName());
            itemData.setProductDescription(product.getDescription());
            itemData.setProductImageFile(product.getImageFile());
            itemData.setProductUnitOfMeasure(product.getUnitOfMeasure());
            
            if (product.getCategory() != null) {
                itemData.setProductCategoryName(product.getCategory().getName());
            }

            itemData.setPrice(BigDecimal.valueOf(itemModel.getPrice()));
            itemData.setQuantity((int) itemModel.getQuantity()); 
            
            itemData.setStatus(itemModel.getStatus()); 
            itemData.setCreated(new Date());
            itemData.setLastUpdated(new Date());

            orderItems.add(itemData);
        }

        for (OrderItemData itemData : orderItems) {
            orderData.addOrderItem(itemData);
        }
        log.info("Added {} snapshot items to the order.", orderItems.size());

        OrderData savedOrder = orderDataRepository.save(orderData);
        log.info("--- Order successfully saved with ID: {} ---", savedOrder.getId());

        orderModel.setId(savedOrder.getId());
        return orderModel;
    }

    @Override
    public Order invoice(Order order) {
        log.warn("invoice() method is not implemented.");
        return null;
    }

    @Override
    public Order pay(Order order) {
        log.warn("pay() method is not implemented.");
        return null;
    }

    @Override
    public Order pick(Order order) {
        log.warn("pick() method is not implemented.");
        return null;
    }

    @Override
    public Order ship(Order order) {
        log.warn("ship() method is not implemented.");
        return null;
    }

    @Override
    public Order complete(Order order) {
        log.warn("complete() method is not implemented.");
        return null;
    }

    @Override
    public Order cancel(Order order) {
        log.warn("cancel() method is not implemented.");
        return null;
    }

    @Override
    public Order suspend(Order order) {
        log.warn("suspend() method is not implemented.");
        return null;
    }

    @Override
    public Order update(Order order) {
        log.warn("update() method is not implemented.");
        return null;
    }
}

