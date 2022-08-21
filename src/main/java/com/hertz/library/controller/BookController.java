package com.hertz.library.controller;

import com.hertz.library.dto.BookDTO;
import com.hertz.library.exceptions.BookAlreadyLoanedException;
import com.hertz.library.exceptions.BookDoesNotExistException;
import com.hertz.library.exceptions.MemberDoesNotExistException;
import com.hertz.library.exceptions.MemberHasNotBorrowedBookException;
import com.hertz.library.exceptions.MemberHasOutstandingLoanException;
import com.hertz.library.exceptions.TooManyBooksBorrowedException;
import com.hertz.library.service.BookService;
import com.hertz.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        try {
            List<BookDTO> books = bookService.findAll();
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        try {
            Optional<BookDTO> book = bookService.findById(id);
            return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<BookDTO> createNewBook(@RequestBody BookDTO bookDTO) {
        try {
            BookDTO newBook = bookService.save(bookDTO);
            return new ResponseEntity<>(newBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        Optional<BookDTO> book = bookService.findById(id);
        if (book.isPresent()) {
            bookService.deleteById(id);
            return new ResponseEntity<>("Book successfully deleted" + id, HttpStatus.OK);
        } else {
            throw new BookDoesNotExistException();
        }
    }

    @PutMapping("/loan/{bookId}")
    public ResponseEntity<String> loanBookToUser(@PathVariable Long bookId,
                                                 @RequestHeader("member_id") Long memberId) {
        bookService.loanBookToMember(bookId, memberId);
        return new ResponseEntity<>("Book " + bookId + " has been loaned to member " + memberId, HttpStatus.OK);
    }

    @PutMapping("/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId,
                                             @RequestHeader("member_id") Long memberId) {
        bookService.returnBook(bookId, memberId);
        return new ResponseEntity<>("Book " + bookId + " has been returned by member " + memberId, HttpStatus.OK);
    }

    @ExceptionHandler(value = BookDoesNotExistException.class)
    public ResponseEntity<String> handleBookDoesNotExistException() {
        return new ResponseEntity<>("Book does not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MemberDoesNotExistException.class)
    public ResponseEntity<String> handleMemberDoesNotExistException() {
        return new ResponseEntity<>("Member does not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MemberHasNotBorrowedBookException.class)
    public ResponseEntity<String> handleMemberHasNotBorrowedBookException() {
        return new ResponseEntity<>("Member has not borrowed this book", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = BookAlreadyLoanedException.class)
    public ResponseEntity<String> handleBookAlreadyLoanedException() {
        return new ResponseEntity<>("Book has already been loaned", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = TooManyBooksBorrowedException.class)
    public ResponseEntity<String> handleTooManyBooksBorrowedException() {
        return new ResponseEntity<>("Member has borrowed too many books and cannot borrow any more until a book is returned", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MemberHasOutstandingLoanException.class)
    public ResponseEntity<String> handleMemberHasOutstandingLoanException() {
        return new ResponseEntity<>("Member has an outstanding book loaned and cannot borrow any more books until all books returned", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
