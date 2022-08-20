package com.hertz.library.mapper;

import com.hertz.library.dto.AuthorDTO;
import com.hertz.library.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDTO toDto(Author author);

    Author toEntity(AuthorDTO authorDTO);

}
