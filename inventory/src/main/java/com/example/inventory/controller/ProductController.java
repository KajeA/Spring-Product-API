package com.example.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.inventory.model.Product;
import com.example.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "buying-requests";

    // All users
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/search")
    public List<Product> getProductsByNameOrBrand(@RequestParam(required = false) String name, @RequestParam(required = false) String brand) {
        if (name != null) {
            return productService.getProductsByName(name);
        } else if (brand != null) {
            return productService.getProductsByBrand(brand);
        }
        return productService.getAllProducts();
    }

    @PostMapping("/buy/{id}")
    public String buyProduct(@PathVariable Long id, @RequestParam int quantity) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getQuantity() >= quantity) {
                product.setQuantity(product.getQuantity() - quantity);
                productService.updateProduct(id, product);

                // Send buying request to Kafka (Comment out for API testing)
//                kafkaTemplate.send(TOPIC, "Product bought: " + product.getName() + ", Quantity: " + quantity);
                return "Purchase successful.";
            } else {
                return "Not enough products in stock.";
            }
        } else {
            return "Product not found.";
        }
    }

    // Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
    }
}
