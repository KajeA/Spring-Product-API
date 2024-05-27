package com.example.inventory.controller;

import com.example.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leftovers")
public class LeftoverController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<com.example.inventory.model.Product> getLeftoverProducts() {
        return productService.getLeftoverProducts();
    }
}
