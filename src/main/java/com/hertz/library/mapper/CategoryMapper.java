package com.hertz.library.mapper;

import com.hertz.library.dto.CategoryDTO;
import com.hertz.library.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDto(Category bookCategory);

    Category toEntity(CategoryDTO bookCategoryDTO);

}
