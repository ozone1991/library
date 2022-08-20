package com.hertz.library.repository;

import com.hertz.library.model.Book;
import com.hertz.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m.books FROM Member m WHERE m.id = ?1")
    List<Book> checkIfMemberHasOutstandingLoans(Long id);
}
