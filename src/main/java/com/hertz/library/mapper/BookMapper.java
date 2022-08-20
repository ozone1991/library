package com.hertz.library.mapper;

import com.hertz.library.dto.BookDTO;
import com.hertz.library.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, CategoryMapper.class})
public interface BookMapper {

    BookDTO toDto(Book book);

    Book toEntity(BookDTO bookDTO);

}
