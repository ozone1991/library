package com.hertz.library.service;

import com.hertz.library.dto.BookDTO;
import com.hertz.library.dto.MemberDTO;
import com.hertz.library.exceptions.BookAlreadyLoanedException;
import com.hertz.library.exceptions.BookDoesNotExistException;
import com.hertz.library.exceptions.MemberDoesNotExistException;
import com.hertz.library.exceptions.MemberHasNotBorrowedBookException;
import com.hertz.library.exceptions.MemberHasOutstandingLoanException;
import com.hertz.library.exceptions.TooManyBooksBorrowedException;
import com.hertz.library.mapper.BookMapper;
import com.hertz.library.mapper.MemberMapper;
import com.hertz.library.model.Book;
import com.hertz.library.model.Member;
import com.hertz.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;

    private final BookRepository bookRepository;

    private final MemberMapper memberMapper;

    private final MemberService memberService;

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<BookDTO> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    public BookDTO save(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        Book saved = bookRepository.save(book);
        return bookMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public void loanBookToMember(Long bookId, Long memberId) {
        Optional<MemberDTO> member = memberService.findById(memberId);
        Optional<BookDTO> book = findById(bookId);
        validateMemberCanBorrowBook(member, book);

        Member memberEntity = memberMapper.toEntity(member.get());
        Book bookToUpdate = bookMapper.toEntity(book.get());

        bookToUpdate.setBookReturnDate();
        bookToUpdate.setMember(memberEntity);

        bookRepository.save(bookToUpdate);
    }

    public void returnBook(Long bookId, Long memberId) {
        Optional<MemberDTO> member = memberService.findById(memberId);
        Optional<BookDTO> book = findById(bookId);
        validateMemberCanReturnBook(member, book);

        Book bookToUpdate = bookMapper.toEntity(book.get());
        bookToUpdate.setReturnDate(null);

        bookRepository.save(bookToUpdate);
    }

    private void validateMemberCanBorrowBook(Optional<MemberDTO> member, Optional<BookDTO> book) {
        if (member.isEmpty()) {
            throw new MemberDoesNotExistException();
        } else if (book.isEmpty()) {
            throw new BookDoesNotExistException();
        } else if (member.get().getBooks().size() >= 3) {
            throw new TooManyBooksBorrowedException();
        } else if (memberHasOutstandingBooksLoaned(member.get().getId())) {
            throw new MemberHasOutstandingLoanException();
        } else if (bookAlreadyLoaned(book.get().getId())) {
            throw new BookAlreadyLoanedException();
        }
    }

    private void validateMemberCanReturnBook(Optional<MemberDTO> member, Optional<BookDTO> book) {
        if (member.isEmpty()) {
            throw new MemberDoesNotExistException();
        } else if (book.isEmpty()) {
            throw new BookDoesNotExistException();
        } else if (!member.get().getBooks().contains(book.get())) {
            throw new MemberHasNotBorrowedBookException();
        }
    }

    private boolean memberHasOutstandingBooksLoaned(Long id) {
        List<Book> books = memberService.getBooksLoanedByUser(id);
        if (books.isEmpty()) {
            return false;
        } else {
            LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
            for (Book book : books) {
                if (book.getReturnDate().isBefore(now)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean bookAlreadyLoaned(Long id) {
        List<Member> members = bookRepository.checkIfBookAlreadyLoaned(id);
        if (members.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
