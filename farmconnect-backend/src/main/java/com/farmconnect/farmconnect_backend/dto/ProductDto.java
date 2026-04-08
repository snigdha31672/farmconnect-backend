package com.farmconnect.farmconnect_backend.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private String description;
    private Double price;
    private String unit;
    private String category;
    private String image;
    private Integer stock;
}
