package com.manalang.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category_data")
@Data
@EqualsAndHashCode(exclude = "products") 
public class CategoryData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<ProductData> products = new HashSet<>();

    public void addProduct(ProductData product) {
        if (product != null) {
            if (products == null) {
                products = new HashSet<>();
            }
            products.add(product);
            product.setCategory(this);
        }
    }
}

