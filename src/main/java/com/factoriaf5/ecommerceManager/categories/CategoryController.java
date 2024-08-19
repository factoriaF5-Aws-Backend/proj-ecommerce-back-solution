package com.factoriaf5.ecommerceManager.categories;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(200).body(categoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Category>> listCategories(){
        return ResponseEntity.status(200).body((List<Category>) categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findCategory(@PathVariable Long id){
        return ResponseEntity.status(200).body(categoryService.findCategory(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(200).body("Category with id: " + id + " is deleted");
    }

    @PostMapping("/{id}")
    public ResponseEntity<Optional<Category>> updateCategory(@RequestBody CategoryRequest categoryRequest, @PathVariable Long id){
        Optional<Category> updatedCategory = (Optional<Category>) categoryService.updateCategory(categoryRequest,id);

        return ResponseEntity.status(200).body(updatedCategory);
    }
}
