package com.manalang.serviceimpl;

import com.manalang.entity.OrderData;
import com.manalang.entity.OrderItemData;
import com.manalang.enums.OrderItemStatus;
import com.manalang.model.OrderItem;
import com.manalang.repository.OrderDataRepository;
import com.manalang.repository.OrderItemDataRepository;
import com.manalang.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemDataRepository orderItemDataRepository;

    @Autowired
    private OrderDataRepository orderDataRepository; 

    private OrderItem translateEntityToDTO(OrderItemData entity) {
        if (entity == null) {
            return null;
        }
        OrderItem dto = new OrderItem();
        
        dto.setId(entity.getId());
        dto.setProductId(entity.getProductId());
        dto.setProductName(entity.getProductName());
        dto.setProductDescription(entity.getProductDescription());
        dto.setProductCategoryName(entity.getProductCategoryName());
        dto.setProductImageFile(entity.getProductImageFile());
        dto.setProductUnitOfMeasure(entity.getProductUnitOfMeasure());
        dto.setStatus(entity.getStatus());
        dto.setCreated(entity.getCreated());
        dto.setLastUpdated(entity.getLastUpdated());

        if (entity.getPrice() != null) {
            dto.setPrice(entity.getPrice().doubleValue());
        }
        dto.setQuantity(entity.getQuantity());

        if (entity.getOrder() != null) {
            dto.setOrderId(entity.getOrder().getId());
            
            if (entity.getOrder().getCustomer() != null) {
                dto.setCustomerId(entity.getOrder().getCustomer().getId());
                dto.setCustomerName(entity.getOrder().getCustomer().getFirstname() + " " + entity.getOrder().getCustomer().getLastname());
            }
        }
        return dto;
    }

    @Override
    public List<OrderItem> getAll() {
        log.info("Fetching all order items.");
        List<OrderItem> orderItems = new ArrayList<>();
        orderItemDataRepository.findAll()
                .forEach(entity -> orderItems.add(translateEntityToDTO(entity)));
        return orderItems;
    }

    @Override
    public List<OrderItem> getOrderItems(Integer customerId) {
        log.info("Fetching 'Ordered' items for customer: {}", customerId);
        
        List<OrderData> customerOrders = orderDataRepository.findByCustomer_Id(customerId);

        List<OrderItem> items = new ArrayList<>();
        for (OrderData order : customerOrders) {
            order.getOrderItems().stream()
                    .filter(itemData -> itemData.getStatus() == OrderItemStatus.Ordered)
                    .map(this::translateEntityToDTO)
                    .forEach(items::add);
        }
        return items;
    }


    @Override
    public List<OrderItem> getCartItems(Integer customerId) {
        log.info("Fetching 'Created' (cart) items for customer: {}", customerId);

        List<OrderData> customerOrders = orderDataRepository.findByCustomer_Id(customerId);
        List<OrderItem> items = new ArrayList<>();
        for (OrderData order : customerOrders) {
            order.getOrderItems().stream()
                    .filter(itemData -> itemData.getStatus() == OrderItemStatus.Created)
                    .map(this::translateEntityToDTO)
                    .forEach(items::add);
        }
        return items;
    }
    
    @Override
    public OrderItem get(Integer id) {
        log.info("Fetching order item by id: {}", id);
        return orderItemDataRepository.findById(id)
                .map(this::translateEntityToDTO) 
                .orElse(null);
    }
    
    @Override
    @Transactional
    public OrderItem create(OrderItem orderItem) {
        log.info("Creating a single order item (Admin/Debug function)");
        
        OrderData orderData = orderDataRepository.findById(orderItem.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderItem.getOrderId()));

        OrderItemData itemData = new OrderItemData();
        itemData.setProductId(orderItem.getProductId());
        itemData.setProductName(orderItem.getProductName());
        itemData.setProductDescription(orderItem.getProductDescription());
        itemData.setProductCategoryName(orderItem.getProductCategoryName());
        itemData.setProductImageFile(orderItem.getProductImageFile());
        itemData.setProductUnitOfMeasure(orderItem.getProductUnitOfMeasure());
        itemData.setPrice(BigDecimal.valueOf(orderItem.getPrice()));
        itemData.setQuantity((int) orderItem.getQuantity());
        itemData.setStatus(orderItem.getStatus() != null ? orderItem.getStatus() : OrderItemStatus.Created);
        
        itemData.setOrder(orderData);
        
        OrderItemData savedItem = orderItemDataRepository.save(itemData);
        return translateEntityToDTO(savedItem);
    }
    
    @Override
    @Transactional
    public List<OrderItem> create(List<OrderItem> orderItems) {
        log.info("Creating a list of {} order items.", orderItems.size());
        return orderItems.stream()
                .map(this::create)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderItem update(OrderItem orderItem) {
        log.info("Updating order item: {}", orderItem.getId());
        
        OrderItemData itemData = orderItemDataRepository.findById(orderItem.getId())
                 .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + orderItem.getId()));
        
        itemData.setQuantity((int) orderItem.getQuantity());
        itemData.setPrice(BigDecimal.valueOf(orderItem.getPrice()));
        itemData.setStatus(orderItem.getStatus());
        
        OrderItemData updatedItem = orderItemDataRepository.save(itemData);
        return translateEntityToDTO(updatedItem);
    }
    
    @Override
    @Transactional
    public List<OrderItem> update(List<OrderItem> orderItems) {
        log.info("Updating a list of {} order items.", orderItems.size());
         return orderItems.stream()
                .map(this::update) 
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<OrderItem> updateStatus(List<Integer> ids, OrderItemStatus orderItemStatus) {
        log.info("Updating status to {} for {} items.", orderItemStatus, ids.size());
        List<OrderItem> updatedItems = new ArrayList<>();
        for (Integer id : ids) {
            orderItemDataRepository.findById(id).ifPresent(itemData -> {
                itemData.setStatus(orderItemStatus);
                OrderItemData savedItem = orderItemDataRepository.save(itemData);
                updatedItems.add(translateEntityToDTO(savedItem));
            });
        }
        return updatedItems;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting order item: {}", id);
        if (orderItemDataRepository.existsById(id)) {
            orderItemDataRepository.deleteById(id);
        } else {
            log.warn("Could not delete. OrderItem not found with id: {}", id);
        }
    }
}

