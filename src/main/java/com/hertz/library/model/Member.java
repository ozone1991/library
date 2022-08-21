package com.hertz.library.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "members")
public class Member extends Person {

    public Member(String firstName, String lastName, String address) {
        super(firstName, lastName);
        this.address = address;
        books = new HashSet<>();
    }

    @NonNull
    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "member")
    private Set<Book> books;

    @Column(name = "account_locked")
    private boolean accountLocked;
}
