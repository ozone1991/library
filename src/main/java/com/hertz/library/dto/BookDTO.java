package com.hertz.library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
public class BookDTO {

    private Long id;

    private String title;

    private AuthorDTO author;

    private Set<CategoryDTO> bookCategories;

    private LocalDateTime returnDate;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BookDTO)) {
            return false;
        }
        BookDTO other = (BookDTO) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
