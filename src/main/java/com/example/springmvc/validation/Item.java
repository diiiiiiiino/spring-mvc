package com.example.springmvc.validation;

import lombok.Data;

@Data
public class Item {
    private Long id;
    private String name;
    private Integer quantity;
    private Integer price;
}
