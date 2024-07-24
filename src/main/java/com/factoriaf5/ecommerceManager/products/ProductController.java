package com.factoriaf5.ecommerceManager.products;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
        Product savedProduct = productService.saveProduct(productRequest);
        return ResponseEntity.status(201).body(savedProduct);
    }

    @GetMapping
    public ResponseEntity<List<Product>> listProducts() {
        List<Product> listOfProducts = productService.getAllProducts();
        return ResponseEntity.status(200).body(listOfProducts);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> findProduct(@PathVariable Long id) {
        Product returnedProduct = productService.findProduct(id);
        return ResponseEntity.status(200).body(returnedProduct);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(200).body("Product with id: " + id + " is deleted");
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Optional<Product>> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id){
        Optional<Product> updatedProduct = productService.updateProduct(productRequest,id);
        return ResponseEntity.status(200).body(updatedProduct);
    }


}
