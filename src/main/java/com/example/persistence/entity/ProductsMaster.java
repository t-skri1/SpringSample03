
package com.example.persistence.entity;

import lombok.Data;

@Data
public class ProductsMaster {
    private String ProductsCode;
    private String ProductsName;
    private Integer UnitPrice;

    public ProductsMaster() {}

    public ProductsMaster(
        String ProductsCode,
        String ProductsName, 
        Integer UnitPrice
        ) {
            this.ProductsCode = ProductsCode;
            this.ProductsName = ProductsName;
            this.UnitPrice = UnitPrice;
        }   
}