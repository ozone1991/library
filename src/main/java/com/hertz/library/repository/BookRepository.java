package com.hertz.library.repository;

import com.hertz.library.model.Book;
import com.hertz.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b.member FROM Book b WHERE b.id=?1")
    List<Member> checkIfBookAlreadyLoaned(Long id);

}
