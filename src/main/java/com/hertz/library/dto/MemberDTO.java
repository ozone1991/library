package com.hertz.library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {

    private Long id;

    private String firstName, lastName, address;

    private Set<BookDTO> books;

    private boolean accountLocked;
}
