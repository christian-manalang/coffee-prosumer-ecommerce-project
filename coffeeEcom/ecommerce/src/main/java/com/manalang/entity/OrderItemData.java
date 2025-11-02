package com.manalang.entity;

import com.manalang.enums.OrderItemStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "order_item_data")
@Data
public class OrderItemData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description", length = 500)
    private String productDescription;

    @Column(name = "product_category_name")
    private String productCategoryName;

    @Column(name = "product_image_file")
    private String productImageFile;

    @Column(name = "product_unit_of_measure")
    private String productUnitOfMeasure;

    @Column(name = "quantity")
    private int quantity; 

    @Column(name = "price", precision = 10, scale = 2) 
    private BigDecimal price; 

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderItemStatus status;

    @CreationTimestamp 
    @Column(name = "date_created")
    private Date created;

    @UpdateTimestamp 
    @Column(name ="last_updated")
    private Date lastUpdated;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderData order;
}

