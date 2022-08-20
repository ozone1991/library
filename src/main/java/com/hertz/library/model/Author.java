package com.hertz.library.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author extends Person {

    public Author(String firstName, String lastName) {
        super(firstName, lastName);
    }
}
