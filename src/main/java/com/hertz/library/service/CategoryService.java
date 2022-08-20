package com.hertz.library.service;

import com.hertz.library.dto.CategoryDTO;
import com.hertz.library.mapper.CategoryMapper;
import com.hertz.library.model.Category;
import com.hertz.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTO> findById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto);
    }

    public CategoryDTO save(CategoryDTO memberDTO) {
        Category entity = categoryMapper.toEntity(memberDTO);
        Category saved = categoryRepository.save(entity);
        return categoryMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
