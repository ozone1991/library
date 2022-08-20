package com.hertz.library.controller;

import com.hertz.library.dto.CategoryDTO;
import com.hertz.library.exceptions.CategoryDoesNotExistException;
import com.hertz.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        try {
            List<CategoryDTO> categories = categoryService.findAll();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        try {
            Optional<CategoryDTO> category = categoryService.findById(id);
            return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createNewCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO newCategory = categoryService.save(categoryDTO);
            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        Optional<CategoryDTO> category = categoryService.findById(id);
        if (category.isPresent()) {
            categoryService.deleteById(id);
            return new ResponseEntity<>("Category successfully deleted " + id, HttpStatus.OK);
        } else {
            throw new CategoryDoesNotExistException();
        }
    }

    @ExceptionHandler(value = CategoryDoesNotExistException.class)
    public ResponseEntity<String> handleCategoryDoesNotExistException() {
        return new ResponseEntity<>("Category does not exist", HttpStatus.NOT_FOUND);
    }
}
